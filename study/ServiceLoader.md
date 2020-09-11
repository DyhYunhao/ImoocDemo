1.ServiceLoader是java提供的一套SPI(Service Provider Interface，常译：服务发现)框架，用于实现服务提供方与服务使用方解耦
2.SPI： 定义一个服务的注册与发现机制
      作用： 通过解耦服务提供者与服务使用者，帮助实现模块化组件化
3.我们都知道JDBC编程有五大基本步骤：
       1.执行数据库驱动类加载：Class.forName("com.mysql.jdbc.driver")
       2.连接数据库： DriverManager.getConnection(url, user, password)
       3.创建SQL语句： Connection#.creatstatement();
       4.执行SQL语句并处理结果集： Statement#executeQuery()
       5.释放资源： ResultSet#close()、Statement#close()与Connection#close()
       操作数据库需要使用厂商提供的数据库驱动程序，直接使用厂商的驱动耦合太强了，更推荐的方法是使用DriveManager管理类：

   步骤1：定义服务接口
         JDBC抽象出一个服务接口，数据库驱动实现类统一实现这个接口：
         public interface Driver {
             // 创建数据库连接
             Connection connect(String url, java.util.Properties info)
                 throws SQLException;

             // 省略其他方法...
         }

