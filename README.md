# Thssdb

## 运行环境
java 8

## 运行方式：
用idea打开，在该目录下运行
```shell
mvn -U install
```
之后找到
```
src/main/java/cn/edu/thssdb/server/ThssDB.java
```
并运行，可能出现
```
Error:(11, 1) java: 程序包cn.edu.thssdb.rpc.thrift不存在
```
的错误，这个时候可能会需要将生成的target文件中的
```
target/generated-sources/thrift
```
该目录在设置中设为sources。
若出现无法找到javafx的问题，可以将所使用的openjdk更换为Oracle jdk或者安装openjdk的openjfx，该问题的出现应该是openjdk中移除了openjfx导致的。
之后可以顺利运行服务端。
之后找到
```
src/main/java/cn/edu/thssdb/client/Client.java
```
文件并运行，但此时会报错
```
[main] ERROR cn.edu.thssdb.client.Client - Missing required options: u, w
```
这是因为我们必须要填入用户名和密码，打开右上角的edit configurations并找到client，在program arguments一行中填入-u root -w 123456即可，之后测试中出现更换用户名登录也与此操作相同。
之后便可以成功启动client程序。

## 注意
进行单元测试之前建议清空data文件夹并关闭thssdb服务端和客户端。而执行TestClient时需要保持服务端运行。
