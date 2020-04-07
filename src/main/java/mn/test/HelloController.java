package mn.test;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.reactivex.mysqlclient.MySQLPool;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowSet;
import io.vertx.sqlclient.PoolOptions;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/hello")
public class HelloController {

  private static final Logger log = LoggerFactory.getLogger(HelloController.class);

  protected MySQLPool _client = null;

  @Value("${aws.secretkeyid}")
  String keyId;

  protected MySQLPool create() {
    if (_client == null) {
      try (ApplicationContext context = ApplicationContext.run()) {
        _client = context.getBean(MySQLPool.class);
      } catch (Throwable e) {
        log.error(e.getMessage());
        e.printStackTrace();
      }
    }
    return _client;
  }

  protected void envPrint() {
    System.out.println("******************************Environment Vars*****************************");
    Map<String, String> enviorntmentVars  = System.getenv();
    enviorntmentVars.entrySet().forEach(System.out::println);
  }

  protected void queryTest() {
    MySQLConnectOptions connectOptions = new MySQLConnectOptions()
        .setPort(3306)
        .setHost("mysql.dev.svc.cluster.local")
        .setDatabase("test")
        .setUser("root")
        .setPassword("password");

// Pool options
    PoolOptions poolOptions = new PoolOptions()
        .setMaxSize(5);

// Create the client pool
    _client = MySQLPool.pool(connectOptions, poolOptions);

// A simple query
    _client
        .query("SELECT * FROM publish limit 1", ar -> {
          if (ar.succeeded()) {
            RowSet<Row> result = ar.result();
            System.out.println("Got " + result.size() + " rows ");
          } else {
            System.out.println("Failure: " + ar.cause().getMessage());
          }
          // Now close the pool
          _client.close();
        });

  }

  @Get(produces = MediaType.TEXT_PLAIN)
  public String index() {
    //envPrint();
    // queryTest();
    create();
    // A simple query
    _client.query("select * from publish limit 1", ar -> {
      if (ar.succeeded()) {
        RowSet<Row> rows = ar.result();
        log.info("SUCCESS!!!!!!!!!!!!!!!!!!");
      } else {
        log.info("FAILED !!!!!!!!!!!!!!!!!!");
      }
    });
    _client.close();
    return "Hello World";
  }
}
