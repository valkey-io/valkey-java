package io.jackey.search.schemafields;

import io.jackey.CommandArguments;
import io.jackey.search.FieldName;
import io.jackey.search.SearchProtocol.SearchKeyword;
import io.jackey.util.SafeEncoder;

public class TagField extends SchemaField {

  private boolean sortable;
  private boolean sortableUNF;
  private boolean noIndex;
  private byte[] separator;
  private boolean caseSensitive;
  private boolean withSuffixTrie;

  public TagField(String fieldName) {
    super(fieldName);
  }

  public TagField(FieldName fieldName) {
    super(fieldName);
  }

  public static TagField of(String fieldName) {
    return new TagField(fieldName);
  }

  public static TagField of(FieldName fieldName) {
    return new TagField(fieldName);
  }

  @Override
  public TagField as(String attribute) {
    super.as(attribute);
    return this;
  }

  /**
   * Sorts the results by the value of this field.
   */
  public TagField sortable() {
    this.sortable = true;
    return this;
  }

  /**
   * Sorts the results by the value of this field without normalization.
   */
  public TagField sortableUNF() {
    this.sortableUNF = true;
    return this;
  }

  /**
   * @see TextField#sortableUNF()
   */
  public TagField sortableUnNormalizedForm() {
    return sortableUNF();
  }

  /**
   * Avoid indexing.
   */
  public TagField noIndex() {
    this.noIndex = true;
    return this;
  }

  /**
   * Indicates how the text contained in the attribute is to be split into individual tags.
   */
  public TagField separator(char separator) {
    if (separator < 128) {
      this.separator = new byte[]{(byte) separator};
    } else {
      this.separator = SafeEncoder.encode(String.valueOf(separator));
    }
    return this;
  }

  /**
   * Keeps the original letter cases of the tags.
   */
  public TagField caseSensitive() {
    this.caseSensitive = true;
    return this;
  }

  /**
   * Keeps a suffix trie with all terms which match the suffix. It is used to optimize
   * <i>contains</i> and <i>suffix</i> queries.
   */
  public TagField withSuffixTrie() {
    this.withSuffixTrie = true;
    return this;
  }

  @Override
  public void addParams(CommandArguments args) {
    args.addParams(fieldName);
    args.add(SearchKeyword.TAG);

    if (separator != null) {
      args.add(SearchKeyword.SEPARATOR).add(separator);
    }

    if (caseSensitive) {
      args.add(SearchKeyword.CASESENSITIVE);
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
