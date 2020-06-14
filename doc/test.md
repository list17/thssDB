create database testDatabase;
show databases;

use testDatabase;
show database testDatabase;

create table student (id int primary key, name varchar(25), school varchar(25), height int, primary key (name));
create table school (sid int primary key, schoolname varchar(25) primary key);
create table instructor(iid int primary key,name varchar(25) not null, salary double, school varchar(25));
create table droptest(idx int primary key);
insert into student values (1, 'lee', 'Tsinghua University', 180);
insert into student (id, name, school) values (2, 'zeng', 'Peking University');
insert into student (id, name, school) values (3, 'sun', 'Tsinghua University');
insert into student (id, name, school) values (4, 'deng', 'Peking University');
insert into student values (5, 'lin', 'Fudan University');
insert into school (sid, schoolname) values (1, 'Tsinghua University');
insert into school values(2, 'Peking University');
insert into school values(3, 'Fudan University');
insert into instructor values (1, 'a', 10000, 'Tsinghua University');
insert into instructor values (2, 'b', 10001, 'Tsinghua University');
insert into instructor values (3, 'c', 10002, 'Tsinghua University');
insert into instructor values (4, 'd', 10003, 'Tsinghua University');
insert into instructor values (5, 'e', 10004, 'Peking University');
insert into instructor values (6, 'f', 10005, 'Fudan University');

select * from school;
select * from instructor;
select * from student;

update student set school = 'Tsinghua University' where school <> 'Tsinghua University';
select * from student;

select id, name, sid from student as a, school as b where a.school = b.schoolname && b.schoolname = 'Tsinghua University';
select a.id, b.sid, c.salary from student as a join school as b on a.school = b.schoolname join instructor as c on c.school = b.schoolname where b.schoolname = 'Tsinghua University';

delete from instructor where salary > 10003;
select * from instructor; -- 查看结果 只剩下4个

drop table droptest;
show database testDatabase; --查看结果 只剩下3个table
drop database testDatabase;
show databases; -- 查看结果 没有database
quit;

## rollback，WAL机制，数据恢复
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

## 用户模块
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