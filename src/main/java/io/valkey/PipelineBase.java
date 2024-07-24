package io.valkey;

/**
 * @deprecated Use {@link AbstractPipeline}.
 */
@Deprecated
public abstract class PipelineBase extends AbstractPipeline {

  protected PipelineBase(CommandObjects commandObjects) {
    super(commandObjects);
  }
}
