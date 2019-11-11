**TSP问题**是指旅行家要旅行n个城市，要求各个城市经历且仅经历一次然后回到出发城市，并要求所走的路程最短。
各个城市间的距离可以用代价矩阵来表示。



![](https://raw.githubusercontent.com/jack13163/GithubPicBed/master/img/20191111131546.png)


![](https://raw.githubusercontent.com/jack13163/GithubPicBed/master/img/20191111131335.png)

* input：算例，包含100-9000个城市。
* App.java:程序入口，cplex调用建模求解过程。
* ConstraintFactory.java:控制子环约束的。
* FileManager.java:读取instance数据的。

输入参数说明：
* --instancePath+空格+算例文件的路径，注意用英文双引号括起来。
* --maximumRead+空格+数字，表示算例大小，也就是多少个城市，文件名可以直接看出。

运行结果如下：

![](https://raw.githubusercontent.com/jack13163/GithubPicBed/master/img/20191111132000.png)