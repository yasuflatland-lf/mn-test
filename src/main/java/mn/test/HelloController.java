package mn.test;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.mysqlclient.MySQLPool;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowIterator;
import io.vertx.reactivex.sqlclient.Tuple;
import java.util.Map;

@Controller("/hello")
public class HelloController {

  private static final Logger log = LoggerFactory.getLogger(HelloController.class);

  protected MySQLPool _client = null;

  @Value("${vertx.mysql.client.port}")
  int vertxPort;

  @Value("${vertx.mysql.client.host}")
  String vertxHost;

  @Value("${vertx.mysql.client.database}")
  String vertxDatabase;

  @Value("${vertx.mysql.client.user}")
  String vertxUser;

  @Value("${vertx.mysql.client.password}")
  String vertxPassword;

  @Value("${vertx.mysql.client.maxSize}")
  int vertxMaxSize;

//  protected void create() {
//
//    MySQLPool client = getClient();
//    client.rxQuery("CREATE TABLE IF NOT EXISTS foo(id INTEGER)").map(rowSet -> {
//      return "test";
//    }).doFinally(() -> {
//      client.close();
//    }).blockingGet();
//
//    MySQLPool client2 = getClient();
//    client2.rxQuery("insert into foo VALUES(1)").map(r2 -> {
//      RowIterator<Row> iterator = r2.iterator();
//      client2.close();
//      return "In";
//    }).blockingGet();
//
//    MySQLPool client3 = getClient();
//    client3.rxQuery("select * from foo").map(r2 -> {
//      RowIterator<Row> iterator = r2.iterator();
//      int id = iterator.next().getInteger("id");
//      client3.close();
//      return "id: ${id}";
//    }).blockingGet();
//
//  }

  protected MySQLPool getClient() {
    ApplicationContext context = ApplicationContext.run();
    return context.getBean(MySQLPool.class);
  }

  protected void create() {

    MySQLPool client = getClient();
    client.rxQuery("CREATE TABLE IF NOT EXISTS foo(id INTEGER)").map(rowSet -> {
      return "test";
    }).doFinally(() -> {
      client.close();
    }).blockingGet();

    MySQLPool client2 = getClient();
    client2.rxQuery("insert into foo VALUES(1)").map(r2 -> {
      RowIterator<Row> iterator = r2.iterator();
      client2.close();
      return "In";
    }).blockingGet();

    MySQLPool client3 = getClient();
    client3
        .rxPreparedQuery("SELECT * FROM foo WHERE id=?", Tuple.of(1))
        .flatMap(rs -> {
          RowIterator<Row> it = rs.iterator();
          int id = it.next().getInteger("id");
          return Single.just(id);
        }).subscribeOn(Schedulers.io()).subscribe(new DisposableSingleObserver<Integer>() {
      @Override
      public void onSuccess(Integer integer) {
        System.out.println(String.valueOf(integer) + " part2");
      }

      @Override
      public void onError(Throwable e) {
        e.printStackTrace();
      }
    });

  }

  protected void envPrint() {
    System.out
        .println("******************************Environment Vars*****************************");
    Map<String, String> enviorntmentVars = System.getenv();
    enviorntmentVars.entrySet().forEach(System.out::println);
  }

//  protected void queryTest() {
//    Map<String, Object> param = new HashMap<String, Object>() {{
//      put("vertx.mysql.client.port", vertxPort);
//      put("vertx.mysql.client.host", vertxHost);
//      put("vertx.mysql.client.database", vertxDatabase);
//      put("vertx.mysql.client.user", vertxUser);
//      put("vertx.mysql.client.password", vertxPassword);
//      put("vertx.mysql.client.maxSize", vertxMaxSize);
//    }};
//
//    param.entrySet().stream().forEach(System.out::println);
//
//    MySQLConnectOptions connectOptions = new MySQLConnectOptions()
//        .setPort(vertxPort)
//        .setHost(vertxHost)
//        .setDatabase(vertxDatabase)
//        .setUser(vertxUser)
//        .setPassword(vertxPassword);
//
//// Pool options
//    PoolOptions poolOptions = new PoolOptions()
//        .setMaxSize(vertxMaxSize);
//
//// Create the client pool
//    _client = MySQLPool.pool(connectOptions, poolOptions);
//
//    return;
//  }

  @Get(produces = MediaType.TEXT_PLAIN)
  public String index() {
    // queryTest();
    create();
    // A simple query
//    _client.getConnection(ar1 -> {
//          if (ar1.succeeded()) {
//            log.info("Connected!!!!!!!!!!");
//          }
//        }
//    );
//    _client.query("CREATE TABLE IF NOT EXISTS foo(id INTEGER)", ar -> {
//      if (ar.succeeded()) {
//        RowSet<Row> rows = ar.result();
//        log.info("SUCCESS!!!!!!!!!!!!!!!!!!");
//      } else {
//        log.info("FAILED !!!!!!!!!!!!!!!!!!");
//      }
//    });
    return "Hello World";
  }
}
