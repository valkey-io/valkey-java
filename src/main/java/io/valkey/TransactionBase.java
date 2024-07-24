package io.valkey;

/**
 * @deprecated Use {@link AbstractTransaction}.
 */
@Deprecated
public abstract class TransactionBase extends AbstractTransaction {

  protected TransactionBase() {
    super();
  }

  protected TransactionBase(CommandObjects commandObjects) {
    super(commandObjects);
  }
}
