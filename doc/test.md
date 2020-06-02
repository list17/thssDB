```sql
create database testDatabase;
show databases;

use testDatabase;
show database testDatabase;

create table student (id int primary key, name varchar(25), school varchar(25), height int, primary key (name));
create table school (sid int primary key, schoolname varchar(25) primary key);
create table instructor(iid int primary key,name varchar(25) not null, salary double, school varchar(25));
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

insert into student values (4); /展示多列主键的限制作用
insert into student values (4, '213', 234, 3); / 数据过多
insert into student values (1, 'lee'); /重复插入

select * from student;
select * from school;
select * from instructor;

select id, name, sid from student as a, school as b where a.school = b.schoolname && b.schoolname = 'Tsinghua University';

select a.id, b.sid, c.salary from student as a join school as b on a.school = b.schoolname join instructor as c on c.school = b.schoolname where b.schoolname = 'Tsinghua University';

update student set school = 'Tsinghua University' where school <> 'Tsinghua University';
select * from student;/查看结果 结果学校全部变为清华

delete from instructor where salary > 10003;
select * from instructor; / 查看结果 只剩下4个

drop table student;
show database testDatabase; /查看结果 只剩下2个table
drop database testDatabase;
show databases; /查看结果 没有database
quit;/只断开了客户端 服务端还在继续运行 可以重启客户端重新连接
/重新连接过后 可以将2-8行全部复制到客户端回车运行，最后可能会需要你多按一次回车 因为前面的回车可能复制到了 最后那一个回车没有复制上所以需要多按一次，然后依次执行
select * from student;
select * from school;
select * from instructor;
查看数据是否有无，然后执行你的事务就行了

create database testDatabase;
use testDatabase;
create table student (id int primary key, name varchar(25), school varchar(25), height int, primary key (name));
select * from testTable;

-- 对照*.script，会写入
insert into testTable (id, name, income) values (21, "name21", 21.21);

-- 对照*.script，暂时未写入
start transaction;
insert into testTable (id, name, income) values (32, "name32", 32.32);
select * from testTable;

delete from testTable where id = 21;
select * from testTable;

update testTable set income = 323.2 where id = 32;
select * from testTable;

rollback;
select * from testTable;

update testTable set income = 212.1 where id = 21;
select * from testTable;

-- 对照*.script，写入update
commit;
select * from testTable;

-- 关闭server

-- 重启，数据恢复
use testDatabase;
select * from testTable;

-- 生成data文件，*.script被删除
shutdown;

-- 重启，数据恢复 (client 1)
use testDatabase;
select * from testTable;

-- 启动另一个client (client 2)，数据一致
use testDatabase;
select * from testTable;

-- client 1
insert into testTable (id, name, income) values (43, "name43", 43.43);
select * from testTable;

-- client 2，数据也增加
select * from testTable;
-- 启动事务
start transaction;
select * from testTable;

-- client 1
-- 被阻塞报错
update testTable set name = "newname43" where id = 43;
-- 未被阻塞
select * from testTable;
-- 被阻塞但不报错
insert into testTable (id, name, income) values (54, "name54", 54.54);

-- client 2，client 1被执行
commit;

-- client 1，数据增加
select * from testTable;
```