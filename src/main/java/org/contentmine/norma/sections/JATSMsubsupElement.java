package org.contentmine.norma.sections;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import nu.xom.Element;

public class JATSMsubsupElement extends AbstractJATSMathmlElement {
    private static final Logger LOG = Logger.getLogger(JATSMsubsupElement.class);
    static {
        LOG.setLevel(Level.DEBUG);
    }

    public static String TAG = "msubsup";

    public JATSMsubsupElement(Element element) {
        super(element);
    }
}