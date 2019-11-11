# 1.什么是VRPTW

提到带时间窗车辆路径问题（vehicle routing problems with time windows，VRPTW），就不得不先说说车辆路径问题（VRP）。

什么是VRP？
车辆路径问题（VRP）最早是由 Dantzig 和 Ramser 于1959年首次提出，它是指一定数量的客户，各自有不同数量的货物需求，配送中心向客户提供货物，由一个车队负责分送货物，组织适当的行车路线，目标是使得客户的需求得到满足，并能在一定的约束下，达到诸如路程最短、成本最小、耗费时间最少等目的。


![](https://raw.githubusercontent.com/jack13163/GithubPicBed/master/img/20191111122546.png)

什么是VRPTW？由于VRP问题的持续发展，考虑需求点对于车辆到达的时间有所要求之下，在车辆途程问题之中加入时窗的限制，便成为带时间窗车辆路径问题（VRP with Time Windows, VRPTW）。
带时间窗车辆路径问题（VRPTW）是在VRP上加上了客户的被访问的时间窗约束。
在VRPTW问题中，除了行驶成本之外, 成本函数还要包括由于早到某个客户而引起的等待时间和客户需要的服务时间。
在VRPTW中，车辆除了要满足VRP问题的限制之外，还必须要满足需求点的时窗限制，而需求点的时窗限制可以分为两种:
* 一种是硬时窗（Hard Time Window），硬时窗要求车辆必须要在时窗内到达，早到必须等待，而迟到则拒收；
* 另一种是软时窗（Soft Time Window），不一定要在时窗内到达，但是在时窗之外到达必须要处罚，以处罚替代等待与拒收是软时窗与硬时窗最大的不同。

# 2.CPLEX求解VRPTW实例

解决带时间窗车辆路径问题（vehicle routing problems with time windows，VRPTW）的常用求解方法：

1. 精确解算法（Exact methods）
    精确解算法解VRPTW问题主要有三个策略，拉格朗日松弛、列生成和动态规划，但是可以求解的算例规模非常小。

2. 途程构建启发式算法（Route-building heuristics）
    在问题中以某节点选择原则或是路线安排原则，将需求点一一纳入途程路线的解法。

3. 途程改善启发式算法（Route-improving heuristics）
    先决定一个可行途程，也就是一个起始解，之后对这个起始解一直做改善，直到不能改善为止。

4. 通用启发式算法（Metaheuristics）
    传统区域搜寻方法的最佳解常因起始解的特性或搜寻方法的限制，而只能获得局部最佳解，为了改善此一缺点，近年来在此领域有重大发展，是新一代的启发式解法，包含禁忌搜索算法（Tabu Search）、模拟退火法（Simulated Annealing）、遗传算法（Genetic Algorithm）和门坎接受法（Threshold Accepting）等，可以有效解决陷入局部最优的困扰。

VRPTW问题建模实例

![](https://raw.githubusercontent.com/jack13163/GithubPicBed/master/img/20191111122332.png)

![](https://raw.githubusercontent.com/jack13163/GithubPicBed/master/img/20191111122305.png)

![](https://raw.githubusercontent.com/jack13163/GithubPicBed/master/img/20191111122325.png)