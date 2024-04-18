package io.jackey.modules.search;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import io.jackey.GeoCoordinate;
import io.jackey.args.GeoUnit;
import io.jackey.search.querybuilder.Node;
import io.jackey.search.querybuilder.QueryBuilders;
import io.jackey.search.querybuilder.Value;
import io.jackey.search.querybuilder.Values;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mnunberg on 2/23/18.
 */
public class QueryBuilderTest {

  @Test
  public void testTag() {
    Value v = Values.tags("foo");
    assertEquals("{foo}", v.toString());
    v = Values.tags("foo", "bar");
    assertEquals("{foo | bar}", v.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyTag() {
    Values.tags();
  }

  @Test
  public void testRange() {
    Value v = Values.between(1, 10);
    assertEquals("[1 10]", v.toString());
    v = Values.between(1, 10).inclusiveMax(false);
    assertEquals("[1 (10]", v.toString());
    v = Values.between(1, 10).inclusiveMin(false);
    assertEquals("[(1 10]", v.toString());

    v = Values.between(1.0, 10.1);
    assertEquals("[1.0 10.1]", v.toString());
    v = Values.between(-1.0, 10.1).inclusiveMax(false);
    assertEquals("[-1.0 (10.1]", v.toString());
    v = Values.between(-1.1, 150.61).inclusiveMin(false);
    assertEquals("[(-1.1 150.61]", v.toString());

    // le, gt, etc.
    // le, gt, etc.
    Assert.assertEquals("[42 42]", Values.eq(42).toString());
    Assert.assertEquals("[-inf (42]", Values.lt(42).toString());
    Assert.assertEquals("[-inf 42]", Values.le(42).toString());
    Assert.assertEquals("[(-42 inf]", Values.gt(-42).toString());
    Assert.assertEquals("[42 inf]", Values.ge(42).toString());

    Assert.assertEquals("[42.0 42.0]", Values.eq(42.0).toString());
    Assert.assertEquals("[-inf (42.0]", Values.lt(42.0).toString());
    Assert.assertEquals("[-inf 42.0]", Values.le(42.0).toString());
    Assert.assertEquals("[(42.0 inf]", Values.gt(42.0).toString());
    Assert.assertEquals("[42.0 inf]", Values.ge(42.0).toString());

    Assert.assertEquals("[(1587058030 inf]", Values.gt(1587058030).toString());

    // string value
    Assert.assertEquals("s", Values.value("s").toString());

    // Geo value
    Assert.assertEquals("[1.0 2.0 3.0 km]",
        Values.geo(new GeoCoordinate(1.0, 2.0), 3.0, GeoUnit.KM).toString());
  }

  @Test
  public void testIntersectionBasic() {
    Node n = QueryBuilders.intersect().add("name", "mark");
    assertEquals("@name:mark", n.toString());

    n = QueryBuilders.intersect().add("name", "mark", "dvir");
    assertEquals("@name:(mark dvir)", n.toString());

    n = QueryBuilders.intersect().add("name", Arrays.asList(Values.value("mark"), Values.value("shay")));
    assertEquals("@name:(mark shay)", n.toString());

    n = QueryBuilders.intersect("name", "meir");
    assertEquals("@name:meir", n.toString());

    n = QueryBuilders.intersect("name", Values.value("meir"), Values.value("rafi"));
    assertEquals("@name:(meir rafi)", n.toString());
  }

  @Test
  public void testIntersectionNested() {
    Node n = QueryBuilders.intersect()
        .add(QueryBuilders.union("name", Values.value("mark"), Values.value("dvir")))
        .add("time", Values.between(100, 200))
        .add(QueryBuilders.disjunct("created", Values.lt(1000)));
    assertEquals("(@name:(mark|dvir) @time:[100 200] -@created:[-inf (1000])", n.toString());
  }

  @Test
  public void testOptional() {
    Node n = QueryBuilders.optional("name", Values.tags("foo", "bar"));
    assertEquals("~@name:{foo | bar}", n.toString());

    n = QueryBuilders.optional(n, n);
    assertEquals("~(~@name:{foo | bar} ~@name:{foo | bar})", n.toString());
  }
}
