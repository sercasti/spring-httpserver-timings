package io.github.sercasti.tracing.domain;

import java.io.IOException;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * Unit tests for {@link HttpServletResponseCopier}.
 */
public class HttpServletResponseCopierTest {

    @Test
    public void testFlushBuffernotNPE() throws IOException {
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final HttpServletResponseCopier copier = new HttpServletResponseCopier(response);
        copier.flushBuffer();
    }

    @Test
    public void testIsCommittednotNPE() throws IOException {
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final HttpServletResponseCopier copier = new HttpServletResponseCopier(response);
        copier.isCommitted();
    }
   
    @Test
    public void testReallyFlushnotNPE() throws IOException {
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final HttpServletResponseCopier copier = new HttpServletResponseCopier(response);
        copier.isCommitted();
    }

}
