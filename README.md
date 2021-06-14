# 集群节点升级

## 问题描述

我们的集群里5000台节点，每个节点上运行了约50个不同的application， 集群上application的种类大概有4000个。

对于生产环境，我们要做到高可用，因此每个application都会运行多份（10到200之间），而且是运行在不同的机器上。

不同的application会定义个可以被同时中断的数目，比如app1有10个replica，我们定义至多可以同时中断2个。

我们真实的生产环境有一个这样的需求：基于安全原因，我们要尽可能快的对集群里面的所有机器的操作系统升级，而升级需要重启机器， 而重启机器就会导致上面运行的application有down time。

因为是生产环境，在不能违反集群里运行的所有application所定义的可被中断的数目下，尽可能找到最快的升级路径。

我们定义如下的数据结构:

```C++

public class DisruptionBudget {

    public int disruptionAllowed;

    public string appName;

}
```

```C++
public class Application {

    public string nodeName;

    public string appName;

}
```

```C++
public class Node {

    public string nodeName;

}
```

其中DisruptionBudget里定义了某个application可以被中断的数目以及appName，如果appName跟Application的appName匹配，就说明这个Application是被他管理的。

比如，集群里有三台节点，运行了3个application，每个app有2个replica，每个app能被同时中断的数目都是1

```text
node list:

[Node{nodeName: node1},

Node{nodeName: node2},

Node{nodeName: node3}]
```

```text
app list:

app1: [Application{appName: app1, nodeName: node1}, Application{appName: app1, nodeName: node2}]

app2: [Application{appName: app2, nodeName: node1}, Application{appName: app2, nodeName: node2}]

app3: [Application{appName: app3, nodeName: node2}, Application{appName: app3, nodeName: node3}]
```

```text
DisruptionBudget list:

[DisruptionBudget{disruptionAllowed: 1, appName: app1},

DisruptionBudget{disruptionAllowed: 1, appName: app2},

DisruptionBudget{disruptionAllowed: 1, appName: app3}]
```

那这个最快升级路径如下：

```text
group #1：[node1, node3]
group #2：[node2]
```

## 要求

介绍解决方案，包括可运行的代码、以及覆盖各种情况的测试用例； 5000台节点&4000个应用的计算能在10min之内完成

## 解决方案

### 方案1： 暴力求解

由于application在节点中的分布随机性较大并且application可中断数目范围没有很强的约束。 因此找到求最优解的快速算法是比较困难的。

一种求解最优解的方案是暴力求解。过程如下：

1. 枚举所有节点的分组情况。
2. 验证每种分组情况是否符合约束条件。
3. 分组数目最小的方案就是最优解。

第一步相当于求基数为n的集合的划分。 划分的数目被称作[贝尔数](https://zh.wikipedia.org/wiki/%E8%B4%9D%E5%B0%94%E6%95%B0).
它的增长速度比一般的指数函数都要快。因此对于有5000个节点的集群，是不可能在10min之内完成的，这不符合要求。

第二步中验证每个分组情况是否符合约束条件可在多项式时间内完成。具体的做法是在每个group中为每个application
维护一个计数器，统计每个group中application的出现次数，再将它与disruptionAllowed进行比较即可。

第三步因为要求最小的解，因此可以从节点分组数目较小的情况开始验证。一旦有出现可行解即为最优解。这样可以缩短暴力求解时间。

总而言之，由于第一步中时间复杂度过高，因此该方案不能成为一个可以接受的方案。

### 方案2： 贪心策略

由于求解最优解的方案时间复杂度过高， 因此我们转而寻求近似算法，利用贪心策略尽可能地求出较快的升级路径。

一般而言，如果每个group包含的节点数比较多，那么最终group的数目也会比较少。 基于这个理论，*我们的贪心策略就是将节点尽可能地塞进一个group中。*

方案如下：

1. 先取一个空group.
2. 遍历所有节点， 验证能否将该节点添加进group。如何可以，则添加节点进入当前group中。
3. 剩下的节点重复上述过程，直到所有节点都属于某一个group.

首先，我们考虑算法的有穷行，也就是能否在有限个步骤后终止。 如果所有节点的disruptionAllowed都大于0，那么每次迭代，group总能够添加一个节点。
如果存在一个节点的disruptionAllowed小于等于0，那么包含该application的节点无论如何都是不能停机的。因此解不存在，可以终止算法。 所以我们上面的算法总是能够终止的。

接着，我们考虑算法的复杂度，因为每次最少能添加一个节点。所以最多分为n个group,n为节点数目。 又每次分组遍历了所有节点，每遍历一个节点需要验证k个application，其中k为节点最大数目。所以它的最糟糕情况下的复杂度是O(
kn^2).

最后，这个方法大多数情况下表现是不错的。它能够解决问题描述中提到的例子。但在下面这个简单例子中，它不能够得到最优解。

|        | node 1 | node 2 | node 3 | node 4 |
|  ----  |  ----  |  ----  |  ----  |  ----  |
| app 1  |        |        |  O     |  O     |
| app 2  | O      |  O     |  O     |  O     |

标记O的地方表示application的replica在该节点上。

```text
DisruptionBudget list:

[DisruptionBudget{disruptionAllowed: 1, appName: app1},

DisruptionBudget{disruptionAllowed: 2, appName: app2}
```

运用上述贪心策略得到的结果为

```text
group #1：[node1, node2]
group #2：[node3]
group #3：[node4]
```

但最优解为

```text
group #1：[node1, node3]
group #2：[node2, node4]
```

### 方案3：启发式算法

在上面的情况中，原有的贪心策略选择了两个价值相对较低的两个节点合并为一组，导致剩下的节点因为过多的约束条件导致多使用了一个group.

那么怎么选择价值更高的节点呢？又如何定义节点的价值呢？我们希望通过节点的价值启发我们更贪心地选择节点。

回到我们的DisruptionBudget. 我们可以通过DisruptionBudget来估算group数目最小值的一个下界。

比如说app1的总数目是r，disruptionAllowed是d,那么我们至少需要`(r+d-1)/d`个group才能符合这个app1的约束条件。

`(r+d-1)/d`就可以看作应用的价值。价值越高，那么这个应用就越应该提前选取，否则由于上述约束条件的存在， 后面还是需要再分为`(r+d-1)/d`个组。

上面仅仅定义了一个application的价值。一个节点包含多个application, 如何定义节点的价值？

节点的价值取决于节点中价值大的application。只有价值大的application减少了，才能在后续的分组中得到更少数目的group.

所以节点价值的比较方法是先求出节点中所有application的价值，然后按价值进行倒序排序，最后按顺序一一比较application的价值。
价值大的application相对应的节点价值也越大。如果相等，再继续比较下一个application。

现在我们已经定义了节点的价值。下面给出启发式算法的流程。

1. 创建一个空的group。
2. 使用一个优先队列存放所有的节点。每次取出价值最高的节点。
3. 验证能否将该节点添加进group。如何可以，则添加节点进入当前group中。
4. 剩下的节点重复上述过程，直到所有节点都属于某一个group.

相比于原有的贪心策略。这里多了一步，每次选择价值最高的节点进入到group中。

值得注意的是，当我们把一个节点放入到group中的时候，它会影响到其他节点的价值。

比如说node1包含了app1. app1的replica是3, disruptionAllowed是2. 那么app1的价值是(3+2-1)/2=2. 假设node1加入到group1中，app1的replica变为2，它的价值变成了(
1+2-1)/2=1.

但庆幸的是，在为一个group选取节点的过程中，由于disruptionAllowed的限制，application的价值最多减少1。因此影响不会太大。

## 实现

实现上，选择较为熟悉的Java语言进行开发，Gradle作为构建工具， JUnit作为测试框架。

### 目录结构

src/main/java/fzp下的目录结构

```text
.
+-- entity
|   +-- greed
    |   +-- GApplication
    |   +-- GGroup
    |   +-- GNode
    |   +-- GReader
|   +-- heuristic
    |   +-- HApplication
    |   +-- HGroup
    |   +-- HNode
    |   +-- HReader
+-- solution
|   +-- GreedSolution.java
|   +-- HeuristicSolution.java
|   +-- HeuristicProSolution.java
+-- utils
|   +-- BestEstimation.java
|   +-- CaseGenerator.java
+-- Application.java
+-- Batch.java
```

entity目录下定义的是算法用到的实体，比如application, group, node等。disruptionAllowed作为属性放入到application的实体里。
另外Reader是用于从文件中读取输入的，后面会介绍文件的具体格式。

另外，greed和heuristic定义不同的实体是为了在设计和实现之初可以更灵活的添加不同的属性以及方法。 后面代码优化可以用父类，接口，组合等方法提取相同或者相似的结构。

solution目录下是具体的算法实现方案。

GreedSolution是方案二。

HeuristicSolution和HeuristicProSolution是方案三。两者的不同是HeuristicProSolution会在选取一个节点放入group后，更新其他节点的价值。 这两者用于比较更新操作对于算法的影响。

utils是用于实验的一些类。

BestEstimation求出所有应用的最大价值。用于估计分组数目最小值的一个下界（最优解至少需要的组数）。

CaseGenerator根据一定的参数随机生成测试样例。

Application作为整个项目的入口。生成的可执行jar包的入口。

Batch用于批量运行测试样例，用于实验。

### 单元测试

单元测试在test目录下。对reader，各种solution以及BestEstimation都做了单元测试。

### 使用方法

命令行执行`gradle jar`在`build/lib`下生成可执行jar包`cluster_upgrade-1.0-SNAPSHOT.jar`。

`cluster_upgrade-1.0-SNAPSHOT.jar`接受两个参数。

第一个参数指定算法， 'g'表示使用greedSolution, 'h'表示使用heuristicSolution, 其他字符表示heuristicProSolution. 第二个参数是测试文件的相对路径。

例子：

```text
java -jar cluster_upgrade-1.0-SNAPSHOT.jar g test0.txt
```

把`cluster_upgrade-1.0-SNAPSHOT.jar`放到`testCase`目录下之下上面命令可以得到输出：

```text
2
n1, n3
n2
```

第一行是组数，后面的每一行是每组中的节点。

## 测试样例

### 生成测试样例

CaseGenerator用于生成测试样例。

主要的生成方法如下：

1. 先计算出整个集群能够容纳的最大replica数量。
2. 从第一个application开始，根据约束随机生成replica的数目r。
3. 随机选择r个有容量的节点，添加application.
4. 生成一个少于r的disruptionAllowed.
5. 重复2-4直到达到集群容量或者application上限.

### 测试样例格式

第一行是节点数n 第二行是application的数量a 后面是a行，第i行两个数用逗号隔开，分别是第i个application的replica数和disruptionAllowed
后面是n行，第j行是第j个节点包含的所有application,逗号隔开

问题描述中的例子可以表示为

```text
3
3
2,1
2,1
2,1
1,2
1,2,3
3
```

### 测试样例说明

testCase下共有41个测试样例。

test0.txt 是问题描述中的例子。

test1.txt 方案2中，贪心策略不能得到的最优解的例子。

test2.txt-test10.txt 节点规模50，application规模40的小规模例子。

test11.txt-text30.txt 节点规模5000，application规模4000,节点容纳50个application，replica在10到r之间。 r随着测试样例序号递增，从10递增到200.
通过r可以控制集群中节点的饱和度。饱和度指节点容纳application的个数。

test31.txt-text40.txt 节点规模50，application规模40。但是它添加了一个特殊application, 包含在所有的节点里。 这个特殊节点能够产生更多区别方案二和方案三的结果。

## 实验

运行Batch.java可以得到下面的实验结果

```text
testCase/test0.txt: 2,2,2,2
testCase/test1.txt: 3,2,2,2
testCase/test2.txt: 3,2,2,2
testCase/test3.txt: 3,3,3,3
testCase/test4.txt: 3,3,3,3
testCase/test5.txt: 5,5,5,5
testCase/test6.txt: 6,6,6,6
testCase/test7.txt: 6,6,6,6
testCase/test8.txt: 7,7,7,7
testCase/test9.txt: 8,8,8,8
testCase/test10.txt: 10,10,10,10
testCase/test11.txt: 13,10,10,10
testCase/test12.txt: 22,20,20,20
testCase/test13.txt: 30,30,30,30
testCase/test14.txt: 40,40,40,40
testCase/test15.txt: 50,50,50,50
testCase/test16.txt: 60,60,60,60
testCase/test17.txt: 70,70,70,70
testCase/test18.txt: 77,77,77,77
testCase/test19.txt: 90,90,90,90
testCase/test20.txt: 98,98,98,98
testCase/test21.txt: 107,107,107,107
testCase/test22.txt: 115,115,115,115
testCase/test23.txt: 130,130,130,130
testCase/test24.txt: 135,135,135,135
testCase/test25.txt: 149,149,149,149
testCase/test26.txt: 147,147,147,147
testCase/test27.txt: 158,158,158,158
testCase/test28.txt: 179,179,179,179
testCase/test29.txt: 182,182,182,182
testCase/test30.txt: 159,159,159,159
testCase/test31.txt: 11,10,10,10
testCase/test32.txt: 11,10,10,10
testCase/test33.txt: 11,10,10,10
testCase/test34.txt: 12,10,10,10
testCase/test35.txt: 15,14,14,14
testCase/test36.txt: 11,10,10,10
testCase/test37.txt: 14,14,14,14
testCase/test38.txt: 15,15,15,15
testCase/test39.txt: 13,12,12,12
testCase/test40.txt: 11,10,10,10
```

对于每个测试样例，后面有四个整数，分别是greedSolution,heuristicSolution,heuristicProSolution和bestEstimation求出的最少分组数目.

实验结论：
1. heuristicSolution在我们的测试样例中都达到了最优解。
2. greedSolution大部分情况下达到了最优解，少部分劣于最优解，但相差不大。
3. heuristicSolution和heuristicProSolution的分组数在我们的测试样例中是一样的，更新操作对结果影响不大。

算法的运行时间在一般配置的电脑上处理5000个节点规模，4000个application规模的问题达到毫秒级或亚秒级。远低于10分钟的要求。

## 一些改进想法

1. 在得到可行解之后，能否通过一些策略和调整方法跳出局部最优解，在更大的解空间里面寻求最优解。 类似于模拟退火，遗传算法，蚁群算法之类。

2. 由于时间关系实验并不是非常完善，测试样例并不是非常多。后续可以添加不同方式的测试样例生成方法，生成各类测试样例，增强实验效果。

3. 输入数据没有做验证，对于错误的输入可能会抛出异常，比如负的节点数，节点中application数多于题目条件等。后续可加强对数据的验证。

## 总结

对于本次的集群节点升级问题，我提供若干种求解算法。 包括暴力解法和基于贪心算法与利用启发式算法的近似算法。 同时还包括完整代码实现，可执行文件，单元测试，实验验证和详尽文档。
