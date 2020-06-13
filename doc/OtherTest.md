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
select building from department where dept_name = 'thss'; # 返回结果为空
commit;
# 第二个客户端执行
select building from department where dept_name = 'thss'; # 返回结果为tsinghua

# 之后你们的额外功能