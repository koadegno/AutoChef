package ulb.infof307.g01.model.exception;

/**
 * Exception lorsque la création du PDF s'est mal passé
 */
public class DocumentException extends Exception{
    public DocumentException(Exception e) {
        super(e);
    }
}
