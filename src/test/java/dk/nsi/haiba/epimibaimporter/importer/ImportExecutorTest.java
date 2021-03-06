/**
 * The MIT License
 *
 * Original work sponsored and donated by National Board of e-Health (NSI), Denmark
 * (http://www.nsi.dk)
 *
 * Copyright (C) 2011 National Board of e-Health (NSI), Denmark (http://www.nsi.dk)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dk.nsi.haiba.epimibaimporter.importer;

import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import dk.nsi.haiba.epimibaimporter.config.EPIMIBATestConfiguration;
import dk.nsi.haiba.epimibaimporter.dao.HAIBADAO;
import dk.nsi.haiba.epimibaimporter.status.ImportStatusRepository;
import dk.nsi.stamdata.jaxws.generated.Answer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class ImportExecutorTest {

    @Configuration
    @Import({ EPIMIBATestConfiguration.class })
    static class TestConfiguration {
        @Bean
        public HAIBADAO haibaDao() {
            return Mockito.mock(HAIBADAO.class);
        }

        @Bean
        public ImportStatusRepository statusRepo() {
            return Mockito.mock(ImportStatusRepository.class);
        }

        @Bean
        public ImportExecutor importExecutor() {
            return new ImportExecutor();
        }
    }

    @Autowired
    ImportExecutor executor;

    @Before
    public void resetMocks() {
    }

    @Test
    public void answerSort() throws Exception {
        Answer a1 = new Answer();
        a1.setTransactionID(new BigInteger("1"));
        Answer a2 = new Answer();
        a2.setTransactionID(new BigInteger("2"));
        Answer a3 = new Answer();
        a3.setTransactionID(new BigInteger("3"));
        List<Answer> list = new ArrayList<Answer>();
        list.add(a3);
        list.add(a2);
        list.add(a1);
        ImportExecutor.SortAnswersByTransactionIdComparator comp = new ImportExecutor.SortAnswersByTransactionIdComparator();
        Collections.sort(list, comp);
        assertTrue(list.get(0).getTransactionID().intValue() == 1);
        assertTrue(list.get(1).getTransactionID().intValue() == 2);
        assertTrue(list.get(2).getTransactionID().intValue() == 3);
    }
}
