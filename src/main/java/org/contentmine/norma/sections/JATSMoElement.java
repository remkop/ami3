package org.contentmine.norma.sections;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import nu.xom.Element;

public class JATSMoElement extends AbstractJATSMathmlElement {
    private static final Logger LOG = Logger.getLogger(JATSMoElement.class);
    static {
        LOG.setLevel(Level.DEBUG);
    }

    public static String TAG = "mo";

    public JATSMoElement(Element element) {
        super(element);
    }
}
