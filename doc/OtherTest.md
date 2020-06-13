# 这个文件是那个其他测试语句pdf稍微修改了一下列名
insert into student(id, dept_name, tot_cred) values ('1', 'Math', 50); # 提示name不能为空
insert into student values('66008', 'Szczerban', 'Languages', 25); # 提示s_id为66008的记录已存在

select name from student where id = '66008';
delete from student where id = '66008';
select name from student where id = '66008';

select name from student where id = '76291'; # 返回结果为Dellwo
update student set name = 'Dell' where id = '76291';
select name from student where id = '76291'; # 返回结果为Dell

show table advisor;
drop table advisor;
select s_id from advisor; # 提示advisor表不存在

start transaction;
insert into department values('thss', 'tsinghua', 1000000.00);
# 第二个客户端执行
select building from department where dept_name = 'thss'; # 客户端卡住，第一个客户端commit后输出结果
commit;
# 第二个客户端执行
select building from department where dept_name = 'thss'; # 返回结果为tsinghua

# 之后你们的额外功能
## rollback，WAL机制，数据恢复
 ```sql
create database testDatabase;
use testDatabase;
create table testTable (id int primary key, name varchar(25), income float, primary key (name));
select * from testTable;

-- 对照testDatabase.script，会写入
insert into testTable (id, name, income) values (21, 'name21', 21.21);

-- 对照testDatabase.script，暂时未写入
start transaction;
insert into testTable (id, name, income) values (32, 'name32', 32.32);
select * from testTable;

delete from testTable where id = 21;
select * from testTable;

update testTable set income = 323.2 where id = 32;
select * from testTable;

-- 回滚成功 显示结果
rollback;
select * from testTable;

-- 修改数据，验证commit成功
update testTable set income = 212.1 where id = 21;
select * from testTable;

-- 对照testDatabase.script，写入update
commit;
select * from testTable;

-- 关闭server

-- 重启，数据恢复
use testDatabase;
select * from testTable;

-- 生成data文件，*.script被删除
shutdown;
```

## 用户模块
``` sql
-- client 1, root登录, server输出用户名
use testDatabase;
create table testschool (sid int primary key, schoolname varchar(25) primary key);
create user roo identified by '123456';

-- client 2, roo登录，server输出用户名
use testDatabase;
-- 报错，无读权限
select * from testschool
-- 报错，无写权限
create table school (sid int primary key, schoolname varchar(25) primary key);

-- client 1, 授权读
grant read on testDatabase to roo

-- client 2
-- 正常执行
select * from testschool
-- 报错，无写权限
create table school (sid int primary key, schoolname varchar(25) primary key);

-- client 1, 授权写
grant write on testDatabase to roo

-- client2, 正常执行
create table school (sid int primary key, schoolname varchar(25) primary key);

-- client1, 收回权限读，写
revoke read, write on testDatabase from roo

-- client2
-- 报错, 无读权限
select * from testschool
-- 报错, 无写权限
create table school (sid int primary key, schoolname varchar(25) primary key);
-- 创建数据库
create database roodb

-- client1
use roodb 
-- 正常执行，说明root有对其他用户所拥有的数据库的操作权限
create table school (sid int primary key, schoolname varchar(25) primary key);

-- 关闭server, 使用错误密码登录roo, 提示密码错误
-- 正确密码登录roo, 正常执行
use roodb
select * from school
```