package ulb.infof307.g01.model.exception;

/**
 * Exception lorsque la création du PDF s'est mal passé
 */
public class PDFException extends Exception{
    public PDFException(Exception e) {
        super(e);
    }
}
