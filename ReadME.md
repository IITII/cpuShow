《面向对象课程设计》任务书6
## 计算机组成原理CPU指令执行演示程序      
> 选题背景: 计算机组成中CPU的组成和结构是核心的内容，机器指令在CPU内部如何执行的是理解程序执行过程的重要环节。 
> 代码结构简单，有时间的话会进行重构（0_o 应该没有时间）
### 功能需求:	
1. 以动画的形式演示单条指令的执行过程，参见P143页；
2. 以动画的形式演示一段汇编语言执行的过程，
3. 演示过程需要用声音、文字等信息表达；
4. 指令需要的执行前的条件和基础数据要可以填写；
5. 使用P143页的CPU模型
### 技术要求	
1. 课程设计必须使用面向对象中的封装性、继承性以及多态性（类、继承、抽象类、接口、多态），且类设计必须合理
2. 可视化一律采用JavaFX（不允许使用Swing或AWT）
3. 所有题目均要设计数据存储，且数据存储采用文件（文本文件或二进制文件）
### 补充说明:	无
### 提交物
1. 系统设计文档（类设计、界面设计、算法设计、文件存储设计）
2. 工程文件夹（源代码、可执行程序、数据存储文件）
3. 课程设计报告
### 实现方法：
Keyword：Timeline，多线程，~~基于文字生成图片~~

### 当前进度

* 大部分代码已完成 & 已打包成jar文件(主要文件只有Main.java和Writer.java)
* 已完成 **MOV,LAD,ADD,STO,JMP指令和汇编语言** 的展示
* [Jar包下载](https://github.com/IITII/cpuShow/releases)

### 使用方法
> 软件要求：Windows已安装JDK8，Ubuntu（Linux）已安装openjdk-8-jdk  
> 其他的版本不做过多解释  
> Gradle项目，如有乱码问题还请自行编译
* Windows：下载后双击即可
* Ubuntu(Linux): 
```bash
sudo apt-get update
sudo apt-get install openjdk-8-jdk
#已安装JDK8的话，可以不进行上面两步
#进入已下载Jar文件的文件夹里
java -jar cpuShow.jar
```
#### 难重点：
1.	Timeline
2.	~~基于文字生成图片~~
3.	固定区域输出提示信息
4. 多线程处理

##### CPU指令
1. MOV指令  
  * 取指阶段：程序计数器PC装入第一条指令的地址101，PC的内容被放到指令地址总线上，对指令进行译码并启动读命令。从101号地址读出MOV指令，通过指令总线IBus装入指令寄存器IR，程序计数器PC内容加1，变成102，为下一条指令做好准备。指令寄存器IR中的操作码被译码，CPU识别出是MOV指令，至此取指阶段完成。
  * 执行阶段：操作控制器OC送出控制信号到通用寄存器，选择R1（10）为源寄存器，RO（00）为目标寄存器。OC送出控制信号到ALU，指定ALU做传送操作，打开ALU输出三态门，将ALU输出（10）送的数据总线DBus上，任何时刻DBus上只能有一个数据。将DBus上的数据打入数据缓冲寄存器DR，将DR中的数据打入目标寄存器RO，RO的内容由00变为10至此MOV指令执行完毕。
2. LAD指令
    * 取指阶段：LAD指令的取指阶段和MOV指令完全相同。
    * 执行阶段：OC发出控制命令，打开IR输出三态门，将指令中的直接地址码6放到数据总线DBus上，装入地址寄存器AR，将数存6号单元中的数100读出到DBus上，装入缓冲寄存器DR。将DR中的数100装入通用寄存器R1，原来R1中的值10被覆盖，至此LAD指令执行完毕。
3. ADD指令
    * 取指阶段：ADD指令的取指阶段和其他指令相同。
    * 执行阶段：操作控制器OC送出控制信号到通用寄存器，选择R1（100）为源寄存器，R2（20）为目标寄存器。ALU做R1和R2的加法运算，打开ALU输出三态门，将运算结果120放到数据总线DBus上，然后打入缓冲寄存器DR。ALU产生的进位信号保存在状态字寄存器PSW中，将DR中数值120装入R2中，R2原来的数20被覆盖。到此ADD指令执行结束。
4. STO指令
    * 取指阶段：STO指令的取指阶段和其他指令相同。
    * 执行阶段：操作控制器OC送出控制信号到通用寄存器，选择R3（30）作为数据存储器的地址。打开通用寄存器输出三态门，将地址30放到DBus上并装入地址寄存器AR，并进行地址译码。操作控制器OC送出控制信号到通用寄存器，选择R2（120）作为数存的写入数据放到DBus上。将数值120写入数存30单元，原先的数据40被冲掉。至此STO指令执行结束。

5. JMP指令
    * 取指阶段：JMP指令的取指周期和其他指令相同。
    * 执行阶段：OC发出控制命令，打开IR输出三态门，将IR中的地址码101发送到DBus上，将DBus上的地址码101打入到程序计数器PC中，PC中原先的地址106被更换。于是下一条指令不是从106单元取出，而是转移到101单元取出。至此JMP指令执行周期结束。