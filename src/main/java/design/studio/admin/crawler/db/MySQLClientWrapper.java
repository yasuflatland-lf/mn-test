package design.studio.admin.crawler.db;

import io.micronaut.context.ApplicationContext;
import io.reactivex.Single;
import io.vertx.reactivex.mysqlclient.MySQLPool;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowSet;

public class MySQLClientWrapper implements AutoCloseable {

  protected MySQLPool _client;

  public MySQLClientWrapper(ApplicationContext context) {
    _client = context.getBean(MySQLPool.class);
  }

  public Single<RowSet<Row>> rxQuery(String sql) {
    return _client.rxQuery(sql);
  }

  @Override
  public void close() throws Exception {
    _client.close();
  }
}
