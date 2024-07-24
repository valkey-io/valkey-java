package io.valkey.search.schemafields;

import io.valkey.CommandArguments;
import io.valkey.search.FieldName;
import io.valkey.search.SearchProtocol.SearchKeyword;

public class TextField extends SchemaField {

  private boolean sortable;
  private boolean sortableUNF;
  private boolean noStem;
  private boolean noIndex;
  private String phoneticMatcher;
  private Double weight;
  private boolean withSuffixTrie;

  public TextField(String fieldName) {
    super(fieldName);
  }

  public TextField(FieldName fieldName) {
    super(fieldName);
  }

  public static TextField of(String fieldName) {
    return new TextField(fieldName);
  }

  public static TextField of(FieldName fieldName) {
    return new TextField(fieldName);
  }

  @Override
  public TextField as(String attribute) {
    super.as(attribute);
    return this;
  }

  /**
   * Sorts the results by the value of this field.
   */
  public TextField sortable() {
    this.sortable = true;
    return this;
  }

  /**
   * Sorts the results by the value of this field without normalization.
   */
  public TextField sortableUNF() {
    this.sortableUNF = true;
    return this;
  }

  /**
   * @see TextField#sortableUNF()
   */
  public TextField sortableUnNormalizedForm() {
    return sortableUNF();
  }

  /**
   * Disable stemming when indexing.
   */
  public TextField noStem() {
    this.noStem = true;
    return this;
  }

  /**
   * Avoid indexing.
   */
  public TextField noIndex() {
    this.noIndex = true;
    return this;
  }

  /**
   * Perform phonetic matching.
   */
  public TextField phonetic(String matcher) {
    this.phoneticMatcher = matcher;
    return this;
  }

  /**
   * Declares the importance of this attribute when calculating result accuracy. This is a
   * multiplication factor.
   */
  public TextField weight(double weight) {
    this.weight = weight;
    return this;
  }

  /**
   * Keeps a suffix trie with all terms which match the suffix. It is used to optimize
   * <i>contains</i> and <i>suffix</i> queries.
   */
  public TextField withSuffixTrie() {
    this.withSuffixTrie = true;
    return this;
  }

  @Override
  public void addParams(CommandArguments args) {
    args.addParams(fieldName);
    args.add(SearchKeyword.TEXT);

    if (weight != null) {
      args.add(SearchKeyword.WEIGHT).add(weight);
    }

    if (noStem) {
      args.add(SearchKeyword.NOSTEM);
    }

    if (phoneticMatcher != null) {
      args.add(SearchKeyword.PHONETIC).add(phoneticMatcher);
    }

    if (withSuffixTrie) {
      args.add(SearchKeyword.WITHSUFFIXTRIE);
    }

    if (sortableUNF) {
      args.add(SearchKeyword.SORTABLE).add(SearchKeyword.UNF);
    } else if (sortable) {
      args.add(SearchKeyword.SORTABLE);
    }

    if (noIndex) {
      args.add(SearchKeyword.NOINDEX);
    }
  }
}
