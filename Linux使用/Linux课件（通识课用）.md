# Linux课件（通识课用）

## 前言

为什么各位的部长让你们来学习Linux操作系统，为无从得知，但是，我可以负责的说，那一定是有好处的。至少，你们完全可以把自己的开发环境迁移到Linux上面来，而不是windows，不是说windows不行，只是在Linux上面搞开发可能更爽吧～（开发我搞不来，不过我之前写过一次考核就是完成一个Linux namespace，我用c语言写出来的代码，最后只能在Linux上面跑，并且还需要Root权限）

闲话少说，书归正传～

## 一、整一个Linux

常规的几个操作一般是下面三个

### （1）虚拟机

在windows下利用虚拟化软件去跑一个虚拟机出来，不过我个人不大建议，这玩意儿怎么用怎么不爽～

### （2）双系统

搞一个Ubuntu（Linux distribution之一，网校大多用这个）+windows双系统  （我看胡仓好像就是这样）

这儿我想吹一下deepin，国人搞的一个Linux发行版，emm，排名挺高的。然后，对于各位习惯了windows的朋友如果要把开发环境迁移过来的话，这系统我稍微比ubuntu推荐一点儿。因为有很多被移植好的软件，QQ，TIM，微信，百度云，爱奇异之类的。

### （3）买一个服务器

这个也是可以的，emm，阿里云学生机才10元/月的样子，1c2g的配置，买来了用ssh协议连过去也是可以的，传输文件的话，可以用scp协议。

上面的三个方案其实如果要长期使用的话，建议第二种。至于装双系统的难度，和重装windows差别不大。不过，可以有其他的办法，这儿提一下，一个就是你去买一个Mac（膜有钱人），一个就是整一块数莓派4B+，最后一个就再配一台主机专门装Linux distribution。

## 二、更新源

更新源在我的个人习惯里面是安装好linux后做的第一件事儿。我假设各位已经有了一个Ubuntu/Debian（Linux distribution）系统在手上，并且，各位能够进入终端或利用SSH（Secure Shell）远程连接上了一个服务器。

### 简介镜像源

**镜像**通常用于为相同信息内容提供不同的**源**，特别**是**在下载量大的时候提供了一种可靠的网络连接。 制作**镜像是**一种文件同步的过程。（理解有难度的话理解为一个第三方提供的软件下载中心，就像应用市场那样的东西，当然这样理解应该是不对的）

### 推荐几个镜像源

阿里Opsx：https://opsx.alibaba.com/mirror

中科大：http://mirrors.ustc.edu.cn/

清华源：https://mirrors.tuna.tsinghua.edu.cn/

......

**重邮源**：http://mirrors.cqupt.edu.cn/ （内网可用）

- 有个问题，为什么要换源呢？

### 换源操作

我们先在不知道Linux命令含义的前提下，跟着把换源操作完成，然后再根据这个换源操作，简要的介绍一下，Linux命令行（关于这个行字，我跑去查了下，我觉得读hang吧，哈哈哈，毕竟英文它叫command line）。

```
# 首先更新一下源

sudo apt update
#更新源是为了下载Vim
sudo apt install vim
#然后切换文件目录到apt的配置文件下
cd /etc/apt
#然后看一下我们的配置文件
ls
#接下来用cp备份一下文件（这儿有个技巧就是输入一点儿点儿东西了按TAB键）
cp sources.list sources.list.bak
#准备修改配置文件
vim sources.list
按下insert键

## #让之前的配置文件失效

(1)
注释掉之前的源信息 用#
(2)
把光标移到最上方疯狂按d键（vim dd命令）

## 或者看看文本有多少行，然后<行数>dd

把自己的源文件copy进来（我习惯用阿里源，如果我有内网机，就一般用的重邮源）
按下esc键
输入:wq   #特别注意，Linux下的所有符号必须半角英语
sudo apt update

#下面是Ubuntu18的镜像配置内容
deb http://mirrors.aliyun.com/ubuntu/ bionic main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ bionic main restricted universe multiverse

deb http://mirrors.aliyun.com/ubuntu/ bionic-security main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ bionic-security main restricted universe multiverse

deb http://mirrors.aliyun.com/ubuntu/ bionic-updates main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ bionic-updates main restricted universe multiverse

deb http://mirrors.aliyun.com/ubuntu/ bionic-proposed main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ bionic-proposed main restricted universe multiverse

deb http://mirrors.aliyun.com/ubuntu/ bionic-backports main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ bionic-backports main restricted universe multiverse
```

上面的操作涉及到了Vim编辑器，这个下去感兴趣的可以自学一下。（我也没学会，命令太多了，用vim少，记不住，捂脸～）

给个链接：[菜鸟教程](https://www.runoob.com/linux/linux-vim.html)

### 换源命令解释

1. #### apt

   apt的全称是Advanced Packaging Tool是Linux系统下的一款安装包管理工具。 最初需要在Linux系统中安装软件，需要自行编译各类软件，缺乏一个统一管理软件包的工具。此后当Debian系统出现后，dpkg管理工具也就被设计出来了，此后为了更加快捷、方便的安装各类软件，dpkg的前端工具APT也出现了。在Ubuntu16.04系统下apt命令功能又得到了强化，使其更加方便快捷和受欢迎了。

   ##### （一）apt update

   **更新软件源中的所有软件列表。因为更新数据库等操作需要管理员的权限，所以在apt命令前要加上sudo命令取得权限。**

   运行apt update命令会返回三种状态：一是命中，一是获取，一是忽略。 命中表示连接上网站，包的信息没有改变。 获取表示有更新并且下载。 忽略表示无更新或更新无关紧要无需更新。 在命令显示的最后，会提示你有多少软件包可以升级，你可以根据自身的需求立即或稍后进行升级。

   ##### （二）apt list

   apt list --upgradeable：显示可升级的软件包。

   apt list --installed：显示已安装的软件包。

   ##### （三）apt upgrade

   执行完update命令后，就可以使用apt upgrade来升级软件包了。执行命令后系统会提示有几个软件需要升级。在得到你的同意后，系统即开始自动下载安装软件包。

   ##### （四）apt install <软件包名>

   安装指定软件。此命令需管理员权限。如果你对名字并不十分清楚，你可以输入软件名的一部分，系统会给出名字相近的软件包名的提示。在老版本中安装程序需要使用`sudo apt-get install <软件包名>`，现在新版本中已建议直接使用apt替代apt-get命令。 `sudo apt install -f` 使用此命令可修复依赖关系，假如有软件因依赖关系不满足而无法安装，就可以运行此命令自动修复安装程序包所依赖的包。特别是在使用dpkg命令安装deb软件包时出现依赖问题常需要此命令来修复

   ##### （五）apt remove <软件包名>和apt autoremove

   apt remove <软件包名>用来卸载指定软件。 apt autoremove用来自动清理不再使用的依赖和库文件。

   ##### （六）apt show <软件包名>

   显示软件包具体信息。例如：版本号，安装大小，依赖关系，bug报告等等。

   ##### （七）apt search  <软件包名>

   可以去搜索想安装的软件～

2. #### cd

   cd [-L|[-P [-e]] [-@]] [目录] 改变 shell 工作目录。

   ```
   改变当前目录至 DIR 目录。默认的 DIR 目录是 shell 变量 HOME
   的值。
   ```

   cd命令一般使用过程中很少带参数，记住`cd <dir>`就行了。

   其中，提醒一下的就是 cd ..是返回上级目录，cd ~ 是进入/root目录

3. #### ls

   ```
   ls [-alrtAFR] [name...]
   ```

   Linux ls命令用于显示指定工作目录下之内容（列出目前工作目录所含之文件及子目录)。

   参数** :

   - -a 显示所有文件及目录 (ls内定将文件名或目录名称开头为"."的视为隐藏档，不会列出)
   - -l 除文件名称外，亦将文件型态、权限、拥有者、文件大小等资讯详细列出
   - -r 将文件以相反次序显示(原定依英文字母次序)
   - -t 将文件依建立时间之先后次序列出
   - -A 同 -a ，但不列出 "." (目前目录) 及 ".." (父目录)
   - -F 在列出的文件名称后加一符号；例如可执行档则加 "*", 目录则加 "/"
   - -R 若目录下有文件，则以下之文件亦皆依序列出

4. #### cp

   Linux cp命令主要用于复制文件或目录。

   ### 语法

   ```
   cp [options] source dest
   ```

   或

   ```
   cp [options] source... directory
   ```

   **参数说明**：

   - -a：此选项通常在复制目录时使用，它保留链接、文件属性，并复制目录下的所有内容。其作用等于dpR参数组合。
   - -d：复制时保留链接。这里所说的链接相当于Windows系统中的快捷方式。
   - -f：覆盖已经存在的目标文件而不给出提示。
   - -i：与-f选项相反，在覆盖目标文件之前给出提示，要求用户确认是否覆盖，回答"y"时目标文件将被覆盖。
   - -p：除复制文件的内容外，还把修改时间和访问权限也复制到新文件中。
   - -r：若给出的源文件是一个目录文件，此时将复制该目录下所有的子目录和文件。
   - -l：不复制文件，只是生成链接文件。

5. #### sudo 

   这个命令一般这么用

   要么加在某些命令行的前面，要么就sudo -s，用来以root权限执行某些操作的。

   - 有个问题就，su 和 su - 也是可以的，但是我推荐sudo，至于为啥呢？你可以看看下面这篇文章
   - [深入理解 sudo 与 su 之间的区别](https://linux.cn/article-8404-1.html)

   **参数说明**：

   - -V 显示版本编号
   - -h 会显示版本编号及指令的使用方式说明
   - -l 显示出自己（执行 sudo 的使用者）的权限
   - -v 因为 sudo 在第一次执行时或是在 N 分钟内没有执行（N 预设为五）会问密码，这个参数是重新做一次确认，如果超过 N 分钟，也会问密码
   - -k 将会强迫使用者在下一次执行 sudo 时问密码（不论有没有超过 N 分钟）
   - -b 将要执行的指令放在背景执行
   - -p prompt 可以更改问密码的提示语，其中 %u 会代换为使用者的帐号名称， %h 会显示主机名称
   - -u username/#uid 不加此参数，代表要以 root 的身份执行指令，而加了此参数，可以以 username 的身份执行指令（#uid 为该 username 的使用者号码）
   - -s 执行环境变数中的 SHELL 所指定的 shell ，或是 /etc/passwd 里所指定的 shell
   - -H 将环境变数中的 HOME （家目录）指定为要变更身份的使用者家目录（如不加 -u 参数就是系统管理者 root ）
   - command 要以系统管理者身份（或以 -u 更改为其他人）执行的指令

## 三、小结与思考

### 小结

我们可以通过前面的5个命令发现，Linux命令行似乎都有这么几个通用的特点

##### 1.语法结构

<命令名> <参数> <后续操作：目录/可执行文件/文本内容>

##### 2.都有使用说明

我们可以在参数说明看到，他们大概都会有 -h 或者 --help 这样的参数，来展示该命令的说明文档。

有了这个小结，我们就可以引出下面我抄来的这点对任何Linux使用者都有用的东西：

### 学会在命令行中获取帮助

在 Linux 环境中，如果你遇到困难，可以使用`man`命令，它是`Manual pages`（手册页）的缩写。

你可以使用如下方式来获得某个命令的说明和使用方式的详细介绍：

```
man <command_name>
```

比如你想查看 man 命令本身的使用方式，你可以输入：

```
man man
```

#### man命令分册

**为便于查找，手册分册处理**

| 区段 | 说明                                     |
| ---- | ---------------------------------------- |
| 1    | 一般命令                                 |
| 2    | 系统调用                                 |
| 3    | 库函数，涵盖了C标准函数库                |
| 4    | 特殊文件（通常是/dev中的设备）和驱动程序 |
| 5    | 文件格式和约定                           |
| 6    | 游戏和屏保                               |
| 7    | 杂项                                     |
| 8    | 系统管理命令和守护进程                   |

要查看相应区段的内容，就在 man 后面加上相应区段的数字即可，如：

```
$ man 1 ls
```

会显示第一区段中的`ls`命令 man 页面。

通常 man 手册中的内容很多，你可能不太容易找到你想要的结果，不过幸运的是man提供了一些帮助你查找内容的快捷命令。

| /<关键字> | 搜索                     |
| --------- | ------------------------ |
| n         | 切换到下一个关键字所在处 |
| shift+n   | 上一个关键字所在处       |
| Space     | 翻页                     |
| Enter     | 向下滚动一行             |
| h         | 显示less工具的使用帮助   |
| k，j      | 进行向前向后滚动一行     |
| q         | 退出                     |

> 想要获得更详细的帮助，你还可以使用`info`命令。
>
> 如果你知道某个命令的作用，只是想快速查看一些它的某个具体参数的作用，那么你可以使用`--help`参数，大部分命令都会带有这个参数。

```
ls --help
```

### 思考

除了上面的几个命令，我们会觉得这对于日常使用Linux还说远远不够，比如，新建文件，新建文件夹，删除文件等一系列操作。所以，我在下面会简要的介绍一下用哪些命令来实现我们的这些常规操作，同时，我在文章末尾给你们挑选了几个常用命令的详解，可以去看一看！

## 四、简要的拓展几个命令

### 处理目录的常用命令

接下来我们就来看几个常见的处理目录的命令吧：

- ls: 列出目录
- cd：切换目录
- pwd：显示目前的目录
- mkdir：创建一个新的目录
- rmdir：删除一个空的目录
- cp: 复制文件或目录
- rm: 移除文件或目录
- mv: 移动文件与目录、文件重命名

### Linux 文件内容查看

Linux系统中使用以下命令来查看文件的内容：

- cat  由第一行开始显示文件内容
- tac  从最后一行开始显示，可以看出 tac 是 cat 的倒着写！
- nl   显示的时候，顺道输出行号！
- more 一页一页的显示文件内容
- less 与 more 类似，但是比 more 更好的是，他可以往前翻页！
- head 只看头几行
- tail 只看尾巴几行

更多的命令，建议去菜鸟教程上面看看。[Linux命令大全](https://www.runoob.com/linux/linux-command-manual.html)

## 五、总结

（一）我太菜了，很多东西只能给你们简单介绍，讲的太深入了，我是不会的，但是我们有运气不错，不是吗？

（二）何不尝试把自己的开发环境搬到Linux上面来？

（三）要不去试一试Deepin吧？支持国产（嘶吼，笑～）！

（四）最好的学习Linux的方法不是去背这些命令，而是实践。

（五）Linux中最讨人喜欢的最帅的最NX的是那个“男人”，不是吗？



## 附录：链接

[鸟哥的Linux私房菜博客](http://linux.vbird.org/vbird/)

[W3C](https://www.w3cschool.cn/linux/linux-filesystem.html)

[菜鸟教程](https://www.runoob.com/linux/linux-tutorial.html)

[运气不错？（手动滑稽）](https://www.google.com/)

[最乱的那个md，然后才有了这个md](https://github.com/tangger2000/HDOJ_ACM/blob/master/Linux.md)

Linux rmdir命令删除空的目录。