/*
 * Copyright 2005,2009 Ivan SZKIBA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ini4j.spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;

import java.net.URL;

import java.nio.charset.Charset;

class IniSource
{
    public static final char INCLUDE_BEGIN = '<';
    public static final char INCLUDE_END = '>';
    public static final char INCLUDE_OPTIONAL = '?';
    private static final char ESCAPE_CHAR = '\\';
    private static final String NEWLINE = "\n";
    private final boolean _allowInclude;
    private URL _base;
    private IniSource _chain;
    private final String _commentChars;
    private final Charset _fileEncoding;
    private final HandlerBase _handler;
    private final LineNumberReader _reader;

    IniSource(InputStream input, HandlerBase handler, boolean includeFlag, String comments, Charset fileEncoding)
    {
        this(new InputStreamReader(input, fileEncoding), handler, includeFlag, comments, fileEncoding);
    }

    IniSource(Reader input, HandlerBase handler, boolean includeFlag, String comments, Charset fileEncoding)
    {
        _reader = new LineNumberReader(input);
        _handler = handler;
        _allowInclude = includeFlag;
        _commentChars = comments;
        _fileEncoding = fileEncoding;
    }

    IniSource(URL input, HandlerBase handler, boolean includeFlag, String comments, Charset fileEncoding) throws IOException
    {
        this(new InputStreamReader(input.openStream(), fileEncoding), handler, includeFlag, comments, fileEncoding);
        _base = input;
    }

    int getLineNumber()
    {
        int ret;

        if (_chain == null)
        {
            ret = _reader.getLineNumber();
        }
        else
        {
            ret = _chain.getLineNumber();
        }

        return ret;
    }

    String readLine() throws IOException
    {
        String line;

        if (_chain == null)
        {
            line = readLineLocal();
        }
        else
        {
            line = _chain.readLine();
            if (line == null)
            {
                _chain = null;
                line = readLine();
            }
        }

        return line;
    }

    private void close() throws IOException
    {
        _reader.close();
    }

    private void handleComment(StringBuilder buff)
    {
        if (buff.length() != 0)
        {
            buff.deleteCharAt(buff.length() - 1);
            _handler.handleComment(buff.toString());
            buff.delete(0, buff.length());
        }
    }

    private String handleInclude(String input) throws IOException
    {
        String line = input;

        if (_allowInclude && (line.length() > 2) && (line.charAt(0) == INCLUDE_BEGIN) && (line.charAt(line.length() - 1) == INCLUDE_END))
        {
            line = line.substring(1, line.length() - 1).trim();
            boolean optional = line.charAt(0) == INCLUDE_OPTIONAL;

            if (optional)
            {
                line = line.substring(1).trim();
            }

            URL loc = (_base == null) ? new URL(line) : new URL(_base, line);

            if (optional)
            {
                try
                {
                    _chain = new IniSource(loc, _handler, _allowInclude, _commentChars, _fileEncoding);
                }
                catch (IOException x)
                {
                    assert true;
                }
                finally
                {
                    line = readLine();
                }
            }
            else
            {
                _chain = new IniSource(loc, _handler, _allowInclude, _commentChars, _fileEncoding);
                line = readLine();
            }
        }

        return line;
    }

    private String readLineLocal() throws IOException
    {
        String line = readLineSkipComments();

        if (line == null)
        {
            close();
        }
        else
        {
            line = handleInclude(line);
        }

        return line;
    }

    private String readLineSkipComments() throws IOException
    {
        String line;
        StringBuilder comment = new StringBuilder();
        StringBuilder buff = new StringBuilder();

        for (line = _reader.readLine(); line != null; line = _reader.readLine())
        {
            line = line.trim();
            if (line.length() == 0)
            {
                handleComment(comment);
            }
            else if ((_commentChars.indexOf(line.charAt(0)) >= 0) && (buff.length() == 0))
            {
                comment.append(line.substring(1));
                comment.append(NEWLINE);
            }
            else
            {
                handleComment(comment);
                int escapeCount = 0;

                for (int i = line.length() - 1; (i >= 0) && (line.charAt(i) == ESCAPE_CHAR); i--)
                {
                    escapeCount++;
                }

                if ((escapeCount & 1) == 0)
                {
                    buff.append(line);
                    line = buff.toString();

                    break;
                }

                buff.append(line.subSequence(0, line.length() - 1));
            }
        }

        // handle end comments
        if ((line == null) && (comment.length() != 0))
        {
            handleComment(comment);
        }

        return line;
    }
}
