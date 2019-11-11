Cplex安装程序已经放在了百度云，可以通过下面的链接下载：

链接：https://pan.baidu.com/s/1Zj0JEFNKC7kwmaaBWeOKtQ 
提取码：k497  

## 1. 加入cplex.jar

- 找到cplex.jar所在目录：C:\software\CPLEX_Studio1263\cplex\lib 
- File -> Project Structure -> Modules -> 新建一个与project同名的module -> add cplex.jar　　

![](https://raw.githubusercontent.com/jack13163/GithubPicBed/master/img/20191111105917.png)

## 2. 配置 -Djava.library.path

- 找到cplex1263.**dll**所在目录
- Run -> Run Configurations -> VM options: -Djava.library.path="D:\Program Files\IBM\ILOG\CPLEX_Studio1263\cplex\bin\x64_win64" （注意有引号）

![](https://raw.githubusercontent.com/jack13163/GithubPicBed/master/img/20191111105700.png)

然后，Run 就成功啦

![](https://raw.githubusercontent.com/jack13163/GithubPicBed/master/img/20191111105722.png)