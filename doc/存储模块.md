# 存储模块设计文档
曾正 李胜涛 郑炜熹

## 结构部分

### Entry

数据表的元素(也就是某列某行的值)实现在`Entry.java`中, 成员变量有:

- `Comparable value`: 该元素的具体值, 可以是`int`, `float`, `string`等Java内置类型.
- `ColumnType type`: 该元素的种类, 在`ColumnType.java`定义了5种类型值(与Java内置类型稍有不同), 用于之后判断是否违反列元素定义.

### Column

数据表列的元信息实现在`Column.java`中, 成员变量有:

- `String name`: 列名, 也就是属性值(Attribute).
- `ColumnType type`: 列值类型.
- `boolean primary`: 是否为主键中的属性.
- `boolean notNull`: 是否存在`NotNull`约束
- `int maxLength`: 最大长度, 当`type`为`ColumnType.STRING`时有效.

### Row

数据表行的实现在`Row.java`中, 成员变量有:

- `ArrayList<Entry> entries`: 数组, 存放该行的数据.

### Table

数据表的实现在`Table.java`中, 重要的成员变量有:

- `String databaseName`: 该表所在的数据库的名称.
- `String tableName`: 该表的名称
- `ArrayList<Column> columns`: 该表的所有列的元信息.
- `BPlusTree<Entry, Row> index`: 用B+树结构建立的索引结构, 实际上也是数据表的数据存放地方(每一行都被B+树的叶子节点所指向).
在建立表的时候, 直接构建关于主键的索引结构, 所有定位到行的操作都通过主键在B+树搜索, 搜索到叶子节点后对该行进行操作, 同时更新索引.
- `int primaryIndex`: 主键所在列的索引下标, 当前仅针对单属性主键.

### Exception

对数据表操作时可能抛出的错误定义在以下的文件:

- `DuplicateKeyException.java`: 定义了主键重复的错误.
- `KeyNotExistException.java`: 定义了键值不存在的错误.
- `ColumnMismatchException.java`: 定义了数据行元素个数与列个数不符合的错误.
- `TypeMismatchException.java`: 定义了数据行元素种类与对应列规定的种类不符合的错误.
- `ConstraintViolatedException.java`: 定义了数据行元素违反约束的错误.
- `FileNotExistException.java`: 定义了反序列化时, 数据库文件不存在的错误.

## 功能部分

本实验的数据库种类为内存数据库, 即所有元素都存放在内存中直接操作. 

### 支持类型

在该数据库的实现中, 每个元素都使用`Entry`类进行存储, 元素具体值使用成员`Comparable value`存储, 意味着基本类型
`Int, Long, Float, Double, String`都能被支持.

### 数据表操作

每个数据表都通过主键建立的B+树来管理表中的数据. 具体操作:

- 数据表的查找操作, 其定义为`public Row findRowByPrimaryKey(Entry primaryKey) throws RuntimeException`, 其中`primaryKey`为想要查找的行的主键.
DBMS会根据该主键查找B+树, 定位到对应的行并返回. 若不存在该行则会抛出一个错误`KeyNotExistException`.
- 数据表的添加数据行, 其定义在`public void insert(Row newRow) throws RuntimeException`, 其中`newRow`为想要插入的新行.
DBMS会在插入之前, 先检查: 
  - B+树中是否已经存在该主键值, 若存在, 则抛出`DuplicateKeyException`.
  - 想要插入的新行, 其元素个数是否与列的个数匹配, 若不匹配, 则抛出`ColumnMismatchException`.
  - 行的每个元素类型是否与列定义的类型匹配, 若不匹配, 则抛出`TypeMismatchException`.
  - 行的每个元素是否违反了该表定义的一些约束. 若违反, 则抛出`ConstraintViolatedException`.
检查完毕后, 再插入该行.
- 数据表的删除数据行, 其定义为`public void delete(Entry deleteKey) throws RuntimeException`, 其中`deleteKey`为想要删除行的主键.
若该主键不存在, 则会抛出`KeyNotExistException`.
- 数据库的更新数据行, 其定义为`public void update(Entry updateKey, Row newRow) throws RuntimeException`, 其中`updateKey`为想要更新行的主键, `newRow`为更新值. DBMS会将该更新操作分解为一个删除操作和一个插入操作:
  - 当`updateKey`不存在B+树中时, 会抛出`KeyNotExistException`.
  - 当`newRow`插入时出现错误(所有insert可能出现的错误), 则会先将删除的行复原, 然后抛出对应的错误.


### 持久化存储

数据表使用Java提供的序列化和反序列化功能来进行数据的持久化存储.

- 序列化定义为`public void serialize() throws IOException`. 执行序列化时, DBMS会根据数据库和表的名字定位到文件系统的某处, 然后通过Java的序列化操作, 依次输入数据表的元素个数(方便之后的反序列化)以及所有行的数据.
- 反序列化定义为`public ArrayList<Row> deserialize() throws IOException, ClassNotFoundException`. 执行反序列化时, DBMS会根据数据库和表的名字定位到文件系统中的数据库文件, 然后通过Java的反序列化操作, 获取数据表行数, 然后依次读取每一行的数据, 并更新B+树.


## 单元测试

该模块的单元测试定义在`tableOperatorTest.java`中. 对数据库的增删改查, 序列化和反序列化操作进行了单元测试. 此次测试磁盘文件存储暂时在`ThssDB/data/database/table`文件中, 助教运行单元测试时可以先清空或删除该文件观察变化.