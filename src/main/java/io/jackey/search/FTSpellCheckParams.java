package io.jackey.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import io.jackey.CommandArguments;
import io.jackey.args.Rawable;
import io.jackey.params.IParams;
import io.jackey.search.SearchProtocol.SearchKeyword;
import io.jackey.util.KeyValue;

public class FTSpellCheckParams implements IParams {

  private Collection<Map.Entry<String, Rawable>> terms;
  private Integer distance;
  private Integer dialect;

  public FTSpellCheckParams() {
  }

  public static FTSpellCheckParams spellCheckParams() {
    return new FTSpellCheckParams();
  }

  /**
   * Specifies an inclusion (INCLUDE) of a custom dictionary.
   */
  public FTSpellCheckParams includeTerm(String dictionary) {
    return addTerm(dictionary, SearchKeyword.INCLUDE);
  }

  /**
   * Specifies an exclusion (EXCLUDE) of a custom dictionary.
   */
  public FTSpellCheckParams excludeTerm(String dictionary) {
    return addTerm(dictionary, SearchKeyword.EXCLUDE);
  }

  /**
   * Specifies an inclusion (INCLUDE) or exclusion (EXCLUDE) of a custom dictionary.
   */
  private FTSpellCheckParams addTerm(String dictionary, Rawable type) {
    if (this.terms == null) {
      this.terms = new ArrayList<>();
    }
    this.terms.add(KeyValue.of(dictionary, type));
    return this;
  }

  /**
   * Maximum Levenshtein distance for spelling suggestions (default: 1, max: 4).
   */
  public FTSpellCheckParams distance(int distance) {
    this.distance = distance;
    return this;
  }

  /**
   * Selects the dialect version under which to execute the query.
   */
  public FTSpellCheckParams dialect(int dialect) {
    this.dialect = dialect;
    return this;
  }

  /**
   * This method will not replace the dialect if it has been already set.
   * @param dialect dialect
   * @return this
   */
  public FTSpellCheckParams dialectOptional(int dialect) {
    if (dialect != 0 && this.dialect == null) {
      this.dialect = dialect;
    }
    return this;
  }

  @Override
  public void addParams(CommandArguments args) {

    if (terms != null) {
      terms.forEach(kv -> args.add(SearchKeyword.TERMS).add(kv.getValue()).add(kv.getKey()));
    }

    if (distance != null) {
      args.add(SearchKeyword.DISTANCE).add(distance);
    }

    if (dialect != null) {
      args.add(SearchKeyword.DIALECT).add(dialect);
    }
  }
}
