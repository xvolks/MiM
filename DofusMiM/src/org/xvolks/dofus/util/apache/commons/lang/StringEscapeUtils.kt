/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xvolks.dofus.util.apache.commons.lang

import java.io.IOException
import java.io.StringWriter
import java.io.Writer


/**
 *
 * Escapes and unescapes `String`s for
 * Java, Java Script, HTML, XML, and SQL.
 *
 * @author Apache Jakarta Turbine
 * @author Purple Technology
 * @author [Alexander Day Chaffee](mailto:alex@purpletech.com)
 * @author Antony Riley
 * @author Helge Tesgaard
 * @author [Sean Brown](sean@boohai.com)
 * @author [Gary Gregory](mailto:ggregory@seagullsw.com)
 * @author Phil Steitz
 * @author Pete Gieser
 * @since 2.0
 * @version $Id: StringEscapeUtils.java,v 1.5 2007-10-05 09:12:32 cct Exp $
 */
/**
 *
 * `StringEscapeUtils` instances should NOT be constructed in
 * standard programming.
 *
 *
 * Instead, the class should be used as:
 * <pre>StringEscapeUtils.escapeJava("foo");</pre>
 *
 *
 * This constructor is public to permit tools that require a JavaBean
 * instance to operate.
 */
class StringEscapeUtils {
    companion object {

        // Java and JavaScript
        //--------------------------------------------------------------------------
        /**
         *
         * Escapes the characters in a `String` using Java String rules.
         *
         *
         * Deals correctly with quotes and control-chars (tab, backslash, cr, ff, etc.)
         *
         *
         * So a tab becomes the characters `'\\'` and
         * `'t'`.
         *
         *
         * The only difference between Java strings and JavaScript strings
         * is that in JavaScript, a single quote must be escaped.
         *
         *
         * Example:
         * <pre>
         * input string: He didn't say, "Stop!"
         * output string: He didn't say, \"Stop!\"
        </pre> *
         *
         *
         * @param str  String to escape values in, may be null
         * @return String with escaped values, `null` if null string input
         */
        fun escapeJava(str: String): String? {
            return escapeJavaStyleString(str, false)
        }

        //--------------------------------------------------------------------------
        /**
         *
         * Escapes the characters in a `String` using Java String rules.
         *
         *
         * Deals correctly with quotes and control-chars (tab, backslash, cr, ff, etc.)
         *
         *
         * So a tab becomes the characters `'\\'` and
         * `'t'`.
         *
         *
         * The only difference between Java strings and JavaScript strings
         * is that in JavaScript, a single quote must be escaped.
         *
         *
         * Example:
         * <pre>
         * input string: He didn't say, "Stop!"
         * output string: He didn't say, \"Stop!\"
        </pre> *
         *
         *
         * @param str  String to escape values in, may be null
         * @return String with escaped values, `null` if null string input
         */
        fun escapeJava(str: String, othersToEscape: CharArray): String {
            return escapeJavaStyleString(str, false, othersToEscape)
        }

        /**
         *
         * Escapes the characters in a `String` using Java String rules to
         * a `Writer`.
         *
         *
         * A `null` string input has no effect.
         *
         * @see .escapeJava
         * @param out  Writer to write escaped string into
         * @param str  String to escape values in, may be null
         * @throws IllegalArgumentException if the Writer is `null`
         * @throws IOException if error occurs on underlying Writer
         */
        @Throws(IOException::class)
        fun escapeJava(out: Writer, str: String) {
            escapeJavaStyleString(out, str, false, null)
        }

        /**
         *
         * Escapes the characters in a `String` using JavaScript String rules.
         *
         * Escapes any values it finds into their JavaScript String form.
         * Deals correctly with quotes and control-chars (tab, backslash, cr, ff, etc.)
         *
         *
         * So a tab becomes the characters `'\\'` and
         * `'t'`.
         *
         *
         * The only difference between Java strings and JavaScript strings
         * is that in JavaScript, a single quote must be escaped.
         *
         *
         * Example:
         * <pre>
         * input string: He didn't say, "Stop!"
         * output string: He didn\'t say, \"Stop!\"
        </pre> *
         *
         *
         * @param str  String to escape values in, may be null
         * @return String with escaped values, `null` if null string input
         */
        fun escapeJavaScript(str: String): String? {
            return escapeJavaStyleString(str, true)
        }

        /**
         *
         * Escapes the characters in a `String` using JavaScript String rules
         * to a `Writer`.
         *
         *
         * A `null` string input has no effect.
         *
         * @see .escapeJavaScript
         * @param out  Writer to write escaped string into
         * @param str  String to escape values in, may be null
         * @throws IllegalArgumentException if the Writer is `null`
         * @throws IOException if error occurs on underlying Writer
         */
        @Throws(IOException::class)
        fun escapeJavaScript(out: Writer, str: String) {
            escapeJavaStyleString(out, str, true)
        }

        /**
         *
         * Worker method for the [.escapeJavaScript] method.
         *
         * @param str String to escape values in, may be null
         * @param escapeSingleQuotes escapes single quotes if `true`
         * @param othersToEscape TODO
         * @return the escaped string
         */
        private fun escapeJavaStyleString(str: String, escapeSingleQuotes: Boolean, othersToEscape: CharArray? = null): String {
            try {
                val writer = StringWriter(str.length * 2)
                escapeJavaStyleString(writer, str, escapeSingleQuotes, othersToEscape)
                return writer.toString()
            } catch (ioe: IOException) {
                // this should never ever happen while writing to a StringWriter
                ioe.printStackTrace()
                return str
            }

        }

        /**
         *
         * Worker method for the [.escapeJavaScript] method.
         *
         * @param out write to receieve the escaped string
         * @param str String to escape values in, may be null
         * @param escapeSingleQuote escapes single quotes if `true`
         * @param othersToEscape TODO
         * @throws IOException if an IOException occurs
         */
        @Throws(IOException::class)
        private fun escapeJavaStyleString(out: Writer?, str: String?, escapeSingleQuote: Boolean, othersToEscape: CharArray? = null) {
            if (out == null) {
                throw IllegalArgumentException("The Writer must not be null")
            }
            if (str == null) {
                return
            }
            val sz: Int
            sz = str.length
            for (i in 0 until sz) {
                val ch = str[i]

                // handle unicode
                if (ch.toInt() > 0xfff) {
                    out.write("\\u" + hex(ch))
                } else if (ch.toInt() > 0xff) {
                    out.write("\\u0" + hex(ch))
                } else if (ch.toInt() > 0x7f) {
                    out.write("\\u00" + hex(ch))
                } else if (ch.toInt() < 32) {
                    when (ch) {
                        '\b' -> {
                            out.write("\\")
                            out.write("b")
                        }
                        '\n' -> {
                            out.write("\\")
                            out.write("n")
                        }
                        '\t' -> {
                            out.write("\\")
                            out.write("t")
                        }
//                        '\f' -> {
//                            out.write("\\")
//                            out.write("f")
//                        }
                        '\r' -> {
                            out.write("\\")
                            out.write("r")
                        }
                        else -> if (ch.toInt() > 0xf) {
                            out.write("\\u00" + hex(ch))
                        } else {
                            out.write("\\u000" + hex(ch))
                        }
                    }
                } else {
                    when (ch) {
                        '\'' -> {
                            if (escapeSingleQuote) {
                                out.write("\\")
                            }
                            out.write("\'")
                        }
                        '"' -> {
                            out.write("\\")
                            out.write("\"")
                        }
                        '\\' -> {
                            out.write("\\")
                            out.write("\\")
                        }
                        else -> {
                            var found = false
                            if (othersToEscape != null) {
                                for (c in othersToEscape) {
                                    if (c == ch) {
                                        found = true
                                        break
                                    }
                                }
                            }

                            if (found) {
                                out.write("\\")
                            }
                            out.write(ch.toInt())
                        }
                    }
                }
            }
        }

        /**
         *
         * Returns an upper case hexadecimal `String` for the given
         * character.
         *
         * @param ch The character to convert.
         * @return An upper case hexadecimal `String`
         */
        private fun hex(ch: Char): String {
            return Integer.toHexString(ch.toInt()).toUpperCase()
        }

        /**
         *
         * Unescapes any Java literals found in the `String`.
         * For example, it will turn a sequence of `'\'` and
         * `'n'` into a newline character, unless the `'\'`
         * is preceded by another `'\'`.
         *
         * @param str  the `String` to unescape, may be null
         * @return a new unescaped `String`, `null` if null string input
         */
        fun unescapeJava(str: String?): String? {
            if (str == null) {
                return null
            }
            try {
                val writer = StringWriter(str.length)
                unescapeJava(writer, str)
                return writer.toString()
            } catch (ioe: IOException) {
                // this should never ever happen while writing to a StringWriter
                ioe.printStackTrace()
                return null
            }

        }

        /**
         *
         * Unescapes any Java literals found in the `String` to a
         * `Writer`.
         *
         *
         * For example, it will turn a sequence of `'\'` and
         * `'n'` into a newline character, unless the `'\'`
         * is preceded by another `'\'`.
         *
         *
         * A `null` string input has no effect.
         *
         * @param out  the `Writer` used to output unescaped characters
         * @param str  the `String` to unescape, may be null
         * @throws IllegalArgumentException if the Writer is `null`
         * @throws IOException if error occurs on underlying Writer
         */
        @Throws(IOException::class)
        fun unescapeJava(out: Writer?, str: String?) {
            if (out == null) {
                throw IllegalArgumentException("The Writer must not be null")
            }
            if (str == null) {
                return
            }
            val sz = str.length
            val unicode = StringBuffer(4)
            var hadSlash = false
            var inUnicode = false
            for (i in 0 until sz) {
                val ch = str[i]
                if (inUnicode) {
                    // if in unicode, then we're reading unicode
                    // values in somehow
                    unicode.append(ch)
                    if (unicode.length == 4) {
                        // unicode now contains the four hex digits
                        // which represents our unicode character
                        try {
                            val value = Integer.parseInt(unicode.toString(), 16)
                            out.write(value.toChar().toInt())
                            unicode.setLength(0)
                            inUnicode = false
                            hadSlash = false
                        } catch (nfe: NumberFormatException) {
                            throw RuntimeException("Unable to parse unicode value: $unicode", nfe)
                        }

                    }
                    continue
                }
                if (hadSlash) {
                    // handle an escaped value
                    hadSlash = false
                    when (ch) {
                        '\\' -> out.write("\\")
                        '\'' -> out.write("\'")
                        '\"' -> out.write("\"")
                        'r' -> out.write("\r")
//                        'f' -> out.write("\f")
                        't' -> out.write("\t")
                        'n' -> out.write("\n")
                        'b' -> out.write("\b")
                        'u' -> {
                            // uh-oh, we're in unicode country....
                            inUnicode = true
                        }
                        else -> out.write(ch.toInt())
                    }
                    continue
                } else if (ch == '\\') {
                    hadSlash = true
                    continue
                }
                out.write(ch.toInt())
            }
            if (hadSlash) {
                // then we're in the weird case of a \ at the end of the
                // string, let's output it anyway.
                out.write("\\")
            }
        }

        /**
         *
         * Unescapes any JavaScript literals found in the `String`.
         *
         *
         * For example, it will turn a sequence of `'\'` and `'n'`
         * into a newline character, unless the `'\'` is preceded by another
         * `'\'`.
         *
         * @see .unescapeJava
         * @param str  the `String` to unescape, may be null
         * @return A new unescaped `String`, `null` if null string input
         */
        fun unescapeJavaScript(str: String): String? {
            return unescapeJava(str)
        }

        /**
         *
         * Unescapes any JavaScript literals found in the `String` to a
         * `Writer`.
         *
         *
         * For example, it will turn a sequence of `'\'` and `'n'`
         * into a newline character, unless the `'\'` is preceded by another
         * `'\'`.
         *
         *
         * A `null` string input has no effect.
         *
         * @see .unescapeJava
         * @param out  the `Writer` used to output unescaped characters
         * @param str  the `String` to unescape, may be null
         * @throws IllegalArgumentException if the Writer is `null`
         * @throws IOException if error occurs on underlying Writer
         */
        @Throws(IOException::class)
        fun unescapeJavaScript(out: Writer, str: String) {
            unescapeJava(out, str)
        }

        // HTML and XML
        //--------------------------------------------------------------------------
        /**
         *
         * Escapes the characters in a `String` using HTML entities.
         *
         *
         *
         * For example:
         *
         *
         * `"bread" & "butter"`
         * becomes:
         *
         *
         * `&quot;bread&quot; &amp; &quot;butter&quot;`.
         *
         *
         *
         * Supports all known HTML 4.0 entities, including funky accents.
         * Note that the commonly used apostrophe escape character (&amp;apos;)
         * is not a legal entity and so is not supported).
         *
         * @param str  the `String` to escape, may be null
         * @return a new escaped `String`, `null` if null string input
         *
         * @see .unescapeHtml
         * @see [ISO Entities](http://hotwired.lycos.com/webmonkey/reference/special_characters/)
         *
         * @see [HTML 3.2 Character Entities for ISO Latin-1](http://www.w3.org/TR/REC-html32.latin1)
         *
         * @see [HTML 4.0 Character entity references](http://www.w3.org/TR/REC-html40/sgml/entities.html)
         *
         * @see [HTML 4.01 Character References](http://www.w3.org/TR/html401/charset.html.h-5.3)
         *
         * @see [HTML 4.01 Code positions](http://www.w3.org/TR/html401/charset.html.code-position)
         */
        fun escapeHtml(str: String?): String? {
            if (str == null) {
                return null
            }
            try {
                val writer = StringWriter((str.length * 1.5).toInt())
                escapeHtml(writer, str)
                return writer.toString()
            } catch (e: IOException) {
                //assert false;
                //should be impossible
                e.printStackTrace()
                return null
            }

        }

        /**
         *
         * Escapes the characters in a `String` using HTML entities and writes
         * them to a `Writer`.
         *
         *
         *
         * For example:
         *
         * `"bread" & "butter"`
         *
         * becomes:
         * `&quot;bread&quot; &amp; &quot;butter&quot;`.
         *
         *
         * Supports all known HTML 4.0 entities, including funky accents.
         * Note that the commonly used apostrophe escape character (&amp;apos;)
         * is not a legal entity and so is not supported).
         *
         * @param writer  the writer receiving the escaped string, not null
         * @param string  the `String` to escape, may be null
         * @throws IllegalArgumentException if the writer is null
         * @throws IOException when `Writer` passed throws the exception from
         * calls to the [Writer.write] methods.
         *
         * @see .escapeHtml
         * @see .unescapeHtml
         * @see [ISO Entities](http://hotwired.lycos.com/webmonkey/reference/special_characters/)
         *
         * @see [HTML 3.2 Character Entities for ISO Latin-1](http://www.w3.org/TR/REC-html32.latin1)
         *
         * @see [HTML 4.0 Character entity references](http://www.w3.org/TR/REC-html40/sgml/entities.html)
         *
         * @see [HTML 4.01 Character References](http://www.w3.org/TR/html401/charset.html.h-5.3)
         *
         * @see [HTML 4.01 Code positions](http://www.w3.org/TR/html401/charset.html.code-position)
         */
        @Throws(IOException::class)
        fun escapeHtml(writer: Writer?, string: String?) {
            if (writer == null) {
                throw IllegalArgumentException("The Writer must not be null.")
            }
            if (string == null) {
                return
            }
            Entities.HTML40.escape(writer, string)
        }

        //-----------------------------------------------------------------------
        /**
         *
         * Unescapes a string containing entity escapes to a string
         * containing the actual Unicode characters corresponding to the
         * escapes. Supports HTML 4.0 entities.
         *
         *
         * For example, the string "&amp;lt;Fran&amp;ccedil;ais&amp;gt;"
         * will become "&lt;Franais&gt;"
         *
         *
         * If an entity is unrecognized, it is left alone, and inserted
         * verbatim into the result string. e.g. "&amp;gt;&amp;zzzz;x" will
         * become "&gt;&amp;zzzz;x".
         *
         * @param str  the `String` to unescape, may be null
         * @return a new unescaped `String`, `null` if null string input
         * @see .escapeHtml
         */
        fun unescapeHtml(str: String?): String? {
            if (str == null) {
                return null
            }
            try {
                val writer = StringWriter((str.length * 1.5).toInt())
                unescapeHtml(writer, str)
                return writer.toString()
            } catch (e: IOException) {
                //assert false;
                //should be impossible
                e.printStackTrace()
                return null
            }

        }

        /**
         *
         * Unescapes a string containing entity escapes to a string
         * containing the actual Unicode characters corresponding to the
         * escapes. Supports HTML 4.0 entities.
         *
         *
         * For example, the string "&amp;lt;Fran&amp;ccedil;ais&amp;gt;"
         * will become "&lt;Franais&gt;"
         *
         *
         * If an entity is unrecognized, it is left alone, and inserted
         * verbatim into the result string. e.g. "&amp;gt;&amp;zzzz;x" will
         * become "&gt;&amp;zzzz;x".
         *
         * @param writer  the writer receiving the unescaped string, not null
         * @param string  the `String` to unescape, may be null
         * @throws IllegalArgumentException if the writer is null
         * @throws IOException if an IOException occurs
         * @see .escapeHtml
         */
        @Throws(IOException::class)
        fun unescapeHtml(writer: Writer?, string: String?) {
            if (writer == null) {
                throw IllegalArgumentException("The Writer must not be null.")
            }
            if (string == null) {
                return
            }
            Entities.HTML40.unescape(writer, string)
        }

        //-----------------------------------------------------------------------
        /**
         *
         * Escapes the characters in a `String` using XML entities.
         *
         *
         * For example: <tt>"bread" & "butter"</tt> =>
         * <tt>&amp;quot;bread&amp;quot; &amp;amp; &amp;quot;butter&amp;quot;</tt>.
         *
         *
         *
         * Supports only the five basic XML entities (gt, lt, quot, amp, apos).
         * Does not support DTDs or external entities.
         *
         *
         * Note that unicode characters greater than 0x7f are currently escaped to
         * their numerical \\u equivalent. This may change in future releases.
         *
         * @param writer  the writer receiving the unescaped string, not null
         * @param str  the `String` to escape, may be null
         * @throws IllegalArgumentException if the writer is null
         * @throws IOException if there is a problem writing
         * @see .unescapeXml
         */
        @Throws(IOException::class)
        fun escapeXml(writer: Writer?, str: String?) {
            if (writer == null) {
                throw IllegalArgumentException("The Writer must not be null.")
            }
            if (str == null) {
                return
            }
            Entities.XML.escape(writer, str)
        }

        /**
         *
         * Escapes the characters in a `String` using XML entities.
         *
         *
         * For example: <tt>"bread" & "butter"</tt> =>
         * <tt>&amp;quot;bread&amp;quot; &amp;amp; &amp;quot;butter&amp;quot;</tt>.
         *
         *
         *
         * Supports only the five basic XML entities (gt, lt, quot, amp, apos).
         * Does not support DTDs or external entities.
         *
         *
         * Note that unicode characters greater than 0x7f are currently escaped to
         * their numerical \\u equivalent. This may change in future releases.
         *
         * @param str  the `String` to escape, may be null
         * @return a new escaped `String`, `null` if null string input
         * @see .unescapeXml
         */
        fun escapeXml(str: String?): String? {
            return if (str == null) {
                null
            } else Entities.XML.escape(str)
        }

        //-----------------------------------------------------------------------
        /**
         *
         * Unescapes a string containing XML entity escapes to a string
         * containing the actual Unicode characters corresponding to the
         * escapes.
         *
         *
         * Supports only the five basic XML entities (gt, lt, quot, amp, apos).
         * Does not support DTDs or external entities.
         *
         *
         * Note that numerical \\u unicode codes are unescaped to their respective
         * unicode characters. This may change in future releases.
         *
         * @param writer  the writer receiving the unescaped string, not null
         * @param str  the `String` to unescape, may be null
         * @throws IllegalArgumentException if the writer is null
         * @throws IOException if there is a problem writing
         * @see .escapeXml
         */
        @Throws(IOException::class)
        fun unescapeXml(writer: Writer?, str: String?) {
            if (writer == null) {
                throw IllegalArgumentException("The Writer must not be null.")
            }
            if (str == null) {
                return
            }
            Entities.XML.unescape(writer, str)
        }

        /**
         *
         * Unescapes a string containing XML entity escapes to a string
         * containing the actual Unicode characters corresponding to the
         * escapes.
         *
         *
         * Supports only the five basic XML entities (gt, lt, quot, amp, apos).
         * Does not support DTDs or external entities.
         *
         *
         * Note that numerical \\u unicode codes are unescaped to their respective
         * unicode characters. This may change in future releases.
         *
         * @param str  the `String` to unescape, may be null
         * @return a new unescaped `String`, `null` if null string input
         * @see .escapeXml
         */
        fun unescapeXml(str: String?): String? {
            return if (str == null) {
                null
            } else Entities.XML.unescape(str)
        }

        //-----------------------------------------------------------------------
        /**
         *
         * Escapes the characters in a `String` to be suitable to pass to
         * an SQL query.
         *
         *
         * For example,
         * <pre>statement.executeQuery("SELECT * FROM MOVIES WHERE TITLE='" +
         * StringEscapeUtils.escapeSql("McHale's Navy") +
         * "'");</pre>
         *
         *
         *
         * At present, this method only turns single-quotes into doubled single-quotes
         * (`"McHale's Navy"` => `"McHale''s Navy"`). It does not
         * handle the cases of percent (%) or underscore (_) for use in LIKE clauses.
         *
         * see http://www.jguru.com/faq/view.jsp?EID=8881
         * @param str  the string to escape, may be null
         * @return a new String, escaped for SQL, `null` if null string input
         */
        fun escapeSql(str: String?): String? {
            return str?.replace("'".toRegex(), "''")
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val string = "feziufzheiu; '\"(fjopfze"
            val escaped = escapeJava(string, charArrayOf(';'))
            println(escaped)
            val unescapeJava = unescapeJava(escaped)
            System.err.println(unescapeJava)
            if (string != unescapeJava) {
                System.err.println("ARG.")
            }
        }
    }
}
/**
 *
 * Worker method for the [.escapeJavaScript] method.
 *
 * @param str String to escape values in, may be null
 * @param escapeSingleQuotes escapes single quotes if `true`
 * @return the escaped string
 */
/**
 *
 * Worker method for the [.escapeJavaScript] method.
 *
 * @param out write to receieve the escaped string
 * @param str String to escape values in, may be null
 * @param escapeSingleQuote escapes single quotes if `true`
 * @throws IOException if an IOException occurs
 */
