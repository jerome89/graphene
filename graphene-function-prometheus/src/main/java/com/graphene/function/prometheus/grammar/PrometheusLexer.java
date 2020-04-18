// Generated from Prometheus.g4 by ANTLR 4.7.2
package com.graphene.function.prometheus.grammar;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PrometheusLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		COMMENT=1, NUMBER=2, DURATION=3, LAND=4, LOR=5, LUNLESS=6, SUM=7, AVG=8, 
		MAX=9, MIN=10, COUNT=11, STDVAR=12, STDDEV=13, OFFSET=14, BY=15, WITHOUT=16, 
		ON=17, IGNORING=18, GROUP_LEFT=19, GROUP_RIGHT=20, BOOL=21, COMMA=22, 
		ASSIGN=23, COLON=24, SEMICOLON=25, BLANK=26, TIMES=27, SPACE=28, NAN=29, 
		INF=30, IDENTIFIER=31, METRIC_IDENTIFIER=32, LEFT_PAREN=33, RIGHT_PAREN=34, 
		LEFT_BRACE=35, RIGHT_BRACE=36, LEFT_BRACKET=37, RIGHT_BRACKET=38, EQL=39, 
		EQL_REGEX=40, NEQ_REGEX=41, NEQ=42, LSS=43, GTR=44, GTE=45, LTE=46, ADD=47, 
		SUB=48, MUL=49, DIV=50, POW=51, MOD=52, DURATION_S=53, DURATION_M=54, 
		DURATION_H=55, DURATION_D=56, DURATION_W=57, DURATION_Y=58, STRING=59, 
		CR=60, NL=61, TAB=62, WS=63, PT=64;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"COMMENT", "NUMBER", "DURATION", "LAND", "LOR", "LUNLESS", "SUM", "AVG", 
			"MAX", "MIN", "COUNT", "STDVAR", "STDDEV", "OFFSET", "BY", "WITHOUT", 
			"ON", "IGNORING", "GROUP_LEFT", "GROUP_RIGHT", "BOOL", "COMMA", "ASSIGN", 
			"COLON", "SEMICOLON", "BLANK", "TIMES", "SPACE", "NAN", "INF", "IDENTIFIER", 
			"METRIC_IDENTIFIER", "LEFT_PAREN", "RIGHT_PAREN", "LEFT_BRACE", "RIGHT_BRACE", 
			"LEFT_BRACKET", "RIGHT_BRACKET", "EQL", "EQL_REGEX", "NEQ_REGEX", "NEQ", 
			"LSS", "GTR", "GTE", "LTE", "ADD", "SUB", "MUL", "DIV", "POW", "MOD", 
			"DURATION_S", "DURATION_M", "DURATION_H", "DURATION_D", "DURATION_W", 
			"DURATION_Y", "STRING", "CR", "NL", "TAB", "WS", "DEGIT", "PT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'#'", null, null, "'AND'", "'or'", "'unless'", "'sum'", "'AVG'", 
			"'MAX'", "'min'", "'count'", "'stdvar'", "'stddev'", "'offset'", "'by'", 
			"'without'", "'on'", "'ignoring'", "'group_left'", "'group_right'", "'bool'", 
			"','", "'='", "':'", "';'", "'_'", "'x'", "'<space>'", null, null, null, 
			null, "'('", "')'", "'{'", "'}'", "'['", "']'", null, "'=~'", "'!~'", 
			"'!='", "'<'", "'>'", "'>='", "'<='", "'+'", "'-'", "'*'", "'/'", "'^'", 
			"'%'", "'s'", "'m'", "'h'", "'d'", "'w'", "'y'", null, "'\r'", "'\n'", 
			null, null, "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "COMMENT", "NUMBER", "DURATION", "LAND", "LOR", "LUNLESS", "SUM", 
			"AVG", "MAX", "MIN", "COUNT", "STDVAR", "STDDEV", "OFFSET", "BY", "WITHOUT", 
			"ON", "IGNORING", "GROUP_LEFT", "GROUP_RIGHT", "BOOL", "COMMA", "ASSIGN", 
			"COLON", "SEMICOLON", "BLANK", "TIMES", "SPACE", "NAN", "INF", "IDENTIFIER", 
			"METRIC_IDENTIFIER", "LEFT_PAREN", "RIGHT_PAREN", "LEFT_BRACE", "RIGHT_BRACE", 
			"LEFT_BRACKET", "RIGHT_BRACKET", "EQL", "EQL_REGEX", "NEQ_REGEX", "NEQ", 
			"LSS", "GTR", "GTE", "LTE", "ADD", "SUB", "MUL", "DIV", "POW", "MOD", 
			"DURATION_S", "DURATION_M", "DURATION_H", "DURATION_D", "DURATION_W", 
			"DURATION_Y", "STRING", "CR", "NL", "TAB", "WS", "PT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	  boolean isParenOpen = false;
	  int parenOpenCount = 0;
	  int parenCloseCount = 0;

	  boolean isBraceOpen = false;
	  int braceOpenCount = 0;
	  int braceCloseCount = 0;

	  boolean isBracketOpen = false;
	  int bracketOpenCount = 0;
	  int bracketCloseCount = 0;

	  boolean useAssignmentStatement = false;


	public PrometheusLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Prometheus.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 0:
			COMMENT_action((RuleContext)_localctx, actionIndex);
			break;
		case 1:
			NUMBER_action((RuleContext)_localctx, actionIndex);
			break;
		case 32:
			LEFT_PAREN_action((RuleContext)_localctx, actionIndex);
			break;
		case 33:
			RIGHT_PAREN_action((RuleContext)_localctx, actionIndex);
			break;
		case 34:
			LEFT_BRACE_action((RuleContext)_localctx, actionIndex);
			break;
		case 35:
			RIGHT_BRACE_action((RuleContext)_localctx, actionIndex);
			break;
		case 36:
			LEFT_BRACKET_action((RuleContext)_localctx, actionIndex);
			break;
		case 37:
			RIGHT_BRACKET_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:

			  int i = 1;
			  int type = _input.LA(i);
			  while(type != EOF) {
			    i++;
			    type = _input.LA(i);
			  }
			  Token o = _factory.create(_tokenFactorySourcePair, COMMENT, _text, _channel, _tokenStartCharIndex, i - 1, _tokenStartLine, _tokenStartCharPositionInLine);
			  emit(o);
			  _input.seek(i);

			break;
		}
	}
	private void NUMBER_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:

			        if (isBraceOpen && !isBracketOpen) {
			          throw new UnexpectedCharInsideBraceException("Numbers are only available for duration");
			        }

			        int type = _input.LA(1);
			        _input.LA(-1);

			        char nextChar = (char) type;
			        if (type != NUMBER && type != EOF && nextChar != ' ') {
			          if (type == DURATION_D || type == DURATION_H || type == DURATION_M || type == DURATION_S || type == DURATION_W || type == DURATION_Y) {
			            // duration
			          } else {
			            throw new BadNumberOrDurationException("bad number or duration syntax");
			          }
			        }
			      
			break;
		}
	}
	private void LEFT_PAREN_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 2:

			  isParenOpen = true;
			  parenOpenCount++;

			break;
		}
	}
	private void RIGHT_PAREN_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 3:

			  isParenOpen = false;
			  parenCloseCount++;

			break;
		}
	}
	private void LEFT_BRACE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 4:

			  if (isBraceOpen) {
			    throw new IllegalBraceException("This is not allowed action. Please check the duplicated left brace.");
			  }

			  isBraceOpen = true;
			  braceOpenCount++;

			break;
		}
	}
	private void RIGHT_BRACE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 5:

			  isBraceOpen = false;
			  braceCloseCount++;

			break;
		}
	}
	private void LEFT_BRACKET_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 6:

			  if (isBracketOpen) {
			    throw new IllegalBracketException("This is not allowed action. Please check the duplicated left bracket.");
			  }

			  isBracketOpen = true;
			  bracketOpenCount++;

			break;
		}
	}
	private void RIGHT_BRACKET_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 7:

			  isBracketOpen = false;
			  bracketCloseCount++;

			break;
		}
	}
	@Override
	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 16:
			return ON_sempred((RuleContext)_localctx, predIndex);
		case 22:
			return ASSIGN_sempred((RuleContext)_localctx, predIndex);
		case 28:
			return NAN_sempred((RuleContext)_localctx, predIndex);
		case 29:
			return INF_sempred((RuleContext)_localctx, predIndex);
		case 31:
			return METRIC_IDENTIFIER_sempred((RuleContext)_localctx, predIndex);
		case 39:
			return EQL_REGEX_sempred((RuleContext)_localctx, predIndex);
		case 40:
			return NEQ_REGEX_sempred((RuleContext)_localctx, predIndex);
		case 41:
			return NEQ_sempred((RuleContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean ON_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return  !isBraceOpen ;
		}
		return true;
	}
	private boolean ASSIGN_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return  useAssignmentStatement = true && !isBraceOpen ;
		}
		return true;
	}
	private boolean NAN_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2:
			return  !isBraceOpen ;
		}
		return true;
	}
	private boolean INF_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 3:
			return  !isBraceOpen ;
		}
		return true;
	}
	private boolean METRIC_IDENTIFIER_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 4:
			return  !isBracketOpen && 0 == bracketOpenCount ;
		}
		return true;
	}
	private boolean EQL_REGEX_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 5:
			return  useAssignmentStatement = true ;
		}
		return true;
	}
	private boolean NEQ_REGEX_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 6:
			return  useAssignmentStatement = true ;
		}
		return true;
	}
	private boolean NEQ_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 7:
			return  useAssignmentStatement = true ;
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2B\u01c7\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\3\2\3\2\3\2\3\3\6\3\u008a\n\3\r\3\16\3"+
		"\u008b\3\3\3\3\6\3\u0090\n\3\r\3\16\3\u0091\3\3\3\3\3\3\3\3\6\3\u0098"+
		"\n\3\r\3\16\3\u0099\3\3\6\3\u009d\n\3\r\3\16\3\u009e\3\3\3\3\3\3\3\3\6"+
		"\3\u00a5\n\3\r\3\16\3\u00a6\3\3\6\3\u00aa\n\3\r\3\16\3\u00ab\3\3\3\3\3"+
		"\3\3\3\5\3\u00b2\n\3\3\4\6\4\u00b5\n\4\r\4\16\4\u00b6\3\4\3\4\3\5\3\5"+
		"\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\t\3"+
		"\t\3\t\3\t\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f"+
		"\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3"+
		"\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3"+
		"\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23\3"+
		"\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3"+
		"\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3"+
		"\26\3\26\3\26\3\27\3\27\3\30\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33\3"+
		"\34\3\34\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3"+
		"\36\3\37\3\37\3\37\3\37\3\37\3 \6 \u0149\n \r \16 \u014a\3 \7 \u014e\n"+
		" \f \16 \u0151\13 \3!\7!\u0154\n!\f!\16!\u0157\13!\3!\3!\6!\u015b\n!\r"+
		"!\16!\u015c\3!\3!\3\"\3\"\3\"\3#\3#\3#\3$\3$\3$\3%\3%\3%\3&\3&\3&\3\'"+
		"\3\'\3\'\3(\3(\3(\5(\u0176\n(\3)\3)\3)\3)\3)\3*\3*\3*\3*\3*\3+\3+\3+\3"+
		"+\3+\3,\3,\3-\3-\3.\3.\3.\3/\3/\3/\3\60\3\60\3\61\3\61\3\62\3\62\3\63"+
		"\3\63\3\64\3\64\3\65\3\65\3\66\3\66\3\67\3\67\38\38\39\39\3:\3:\3;\3;"+
		"\3<\6<\u01aa\n<\r<\16<\u01ab\3=\3=\3=\3=\3>\3>\3>\3>\3?\6?\u01b7\n?\r"+
		"?\16?\u01b8\3?\3?\3@\6@\u01be\n@\r@\16@\u01bf\3@\3@\3A\3A\3B\3B\2\2C\3"+
		"\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37"+
		"\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37="+
		" ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64g\65i\66k\67m8o9"+
		"q:s;u<w=y>{?}@\177A\u0081\2\u0083B\3\2\r\3\2\63;\b\2ffjjoouuyy{{\4\2P"+
		"Ppp\4\2CCcc\4\2KKkk\4\2HHhh\5\2C\\aac|\3\2\62;\4\2C\\c|\5\2\62;C\\c|\t"+
		"\2$$))\60\60<<C\\^^b|\2\u01da\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t"+
		"\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2"+
		"\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2"+
		"\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2"+
		"+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2"+
		"\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2"+
		"C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3"+
		"\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2"+
		"\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2"+
		"i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3"+
		"\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2\177\3\2\2\2\2\u0083"+
		"\3\2\2\2\3\u0085\3\2\2\2\5\u00b1\3\2\2\2\7\u00b4\3\2\2\2\t\u00ba\3\2\2"+
		"\2\13\u00be\3\2\2\2\r\u00c1\3\2\2\2\17\u00c8\3\2\2\2\21\u00cc\3\2\2\2"+
		"\23\u00d0\3\2\2\2\25\u00d4\3\2\2\2\27\u00d8\3\2\2\2\31\u00de\3\2\2\2\33"+
		"\u00e5\3\2\2\2\35\u00ec\3\2\2\2\37\u00f3\3\2\2\2!\u00f6\3\2\2\2#\u00fe"+
		"\3\2\2\2%\u0103\3\2\2\2\'\u010c\3\2\2\2)\u0117\3\2\2\2+\u0123\3\2\2\2"+
		"-\u0128\3\2\2\2/\u012a\3\2\2\2\61\u012d\3\2\2\2\63\u012f\3\2\2\2\65\u0131"+
		"\3\2\2\2\67\u0133\3\2\2\29\u0135\3\2\2\2;\u013d\3\2\2\2=\u0142\3\2\2\2"+
		"?\u0148\3\2\2\2A\u0155\3\2\2\2C\u0160\3\2\2\2E\u0163\3\2\2\2G\u0166\3"+
		"\2\2\2I\u0169\3\2\2\2K\u016c\3\2\2\2M\u016f\3\2\2\2O\u0175\3\2\2\2Q\u0177"+
		"\3\2\2\2S\u017c\3\2\2\2U\u0181\3\2\2\2W\u0186\3\2\2\2Y\u0188\3\2\2\2["+
		"\u018a\3\2\2\2]\u018d\3\2\2\2_\u0190\3\2\2\2a\u0192\3\2\2\2c\u0194\3\2"+
		"\2\2e\u0196\3\2\2\2g\u0198\3\2\2\2i\u019a\3\2\2\2k\u019c\3\2\2\2m\u019e"+
		"\3\2\2\2o\u01a0\3\2\2\2q\u01a2\3\2\2\2s\u01a4\3\2\2\2u\u01a6\3\2\2\2w"+
		"\u01a9\3\2\2\2y\u01ad\3\2\2\2{\u01b1\3\2\2\2}\u01b6\3\2\2\2\177\u01bd"+
		"\3\2\2\2\u0081\u01c3\3\2\2\2\u0083\u01c5\3\2\2\2\u0085\u0086\7%\2\2\u0086"+
		"\u0087\b\2\2\2\u0087\4\3\2\2\2\u0088\u008a\5\u0081A\2\u0089\u0088\3\2"+
		"\2\2\u008a\u008b\3\2\2\2\u008b\u0089\3\2\2\2\u008b\u008c\3\2\2\2\u008c"+
		"\u008d\3\2\2\2\u008d\u008f\5\u0083B\2\u008e\u0090\5\u0081A\2\u008f\u008e"+
		"\3\2\2\2\u0090\u0091\3\2\2\2\u0091\u008f\3\2\2\2\u0091\u0092\3\2\2\2\u0092"+
		"\u00b2\3\2\2\2\u0093\u0094\7\62\2\2\u0094\u0095\7z\2\2\u0095\u0097\3\2"+
		"\2\2\u0096\u0098\5\u0081A\2\u0097\u0096\3\2\2\2\u0098\u0099\3\2\2\2\u0099"+
		"\u0097\3\2\2\2\u0099\u009a\3\2\2\2\u009a\u00b2\3\2\2\2\u009b\u009d\5\u0081"+
		"A\2\u009c\u009b\3\2\2\2\u009d\u009e\3\2\2\2\u009e\u009c\3\2\2\2\u009e"+
		"\u009f\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0\u00a1\5\u0083B\2\u00a1\u00b2"+
		"\3\2\2\2\u00a2\u00a4\5\u0083B\2\u00a3\u00a5\5\u0081A\2\u00a4\u00a3\3\2"+
		"\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a4\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7"+
		"\u00b2\3\2\2\2\u00a8\u00aa\5\u0081A\2\u00a9\u00a8\3\2\2\2\u00aa\u00ab"+
		"\3\2\2\2\u00ab\u00a9\3\2\2\2\u00ab\u00ac\3\2\2\2\u00ac\u00ad\3\2\2\2\u00ad"+
		"\u00ae\b\3\3\2\u00ae\u00b2\3\2\2\2\u00af\u00b2\5;\36\2\u00b0\u00b2\5="+
		"\37\2\u00b1\u0089\3\2\2\2\u00b1\u0093\3\2\2\2\u00b1\u009c\3\2\2\2\u00b1"+
		"\u00a2\3\2\2\2\u00b1\u00a9\3\2\2\2\u00b1\u00af\3\2\2\2\u00b1\u00b0\3\2"+
		"\2\2\u00b2\6\3\2\2\2\u00b3\u00b5\t\2\2\2\u00b4\u00b3\3\2\2\2\u00b5\u00b6"+
		"\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7\u00b8\3\2\2\2\u00b8"+
		"\u00b9\t\3\2\2\u00b9\b\3\2\2\2\u00ba\u00bb\7C\2\2\u00bb\u00bc\7P\2\2\u00bc"+
		"\u00bd\7F\2\2\u00bd\n\3\2\2\2\u00be\u00bf\7q\2\2\u00bf\u00c0\7t\2\2\u00c0"+
		"\f\3\2\2\2\u00c1\u00c2\7w\2\2\u00c2\u00c3\7p\2\2\u00c3\u00c4\7n\2\2\u00c4"+
		"\u00c5\7g\2\2\u00c5\u00c6\7u\2\2\u00c6\u00c7\7u\2\2\u00c7\16\3\2\2\2\u00c8"+
		"\u00c9\7u\2\2\u00c9\u00ca\7w\2\2\u00ca\u00cb\7o\2\2\u00cb\20\3\2\2\2\u00cc"+
		"\u00cd\7C\2\2\u00cd\u00ce\7X\2\2\u00ce\u00cf\7I\2\2\u00cf\22\3\2\2\2\u00d0"+
		"\u00d1\7O\2\2\u00d1\u00d2\7C\2\2\u00d2\u00d3\7Z\2\2\u00d3\24\3\2\2\2\u00d4"+
		"\u00d5\7o\2\2\u00d5\u00d6\7k\2\2\u00d6\u00d7\7p\2\2\u00d7\26\3\2\2\2\u00d8"+
		"\u00d9\7e\2\2\u00d9\u00da\7q\2\2\u00da\u00db\7w\2\2\u00db\u00dc\7p\2\2"+
		"\u00dc\u00dd\7v\2\2\u00dd\30\3\2\2\2\u00de\u00df\7u\2\2\u00df\u00e0\7"+
		"v\2\2\u00e0\u00e1\7f\2\2\u00e1\u00e2\7x\2\2\u00e2\u00e3\7c\2\2\u00e3\u00e4"+
		"\7t\2\2\u00e4\32\3\2\2\2\u00e5\u00e6\7u\2\2\u00e6\u00e7\7v\2\2\u00e7\u00e8"+
		"\7f\2\2\u00e8\u00e9\7f\2\2\u00e9\u00ea\7g\2\2\u00ea\u00eb\7x\2\2\u00eb"+
		"\34\3\2\2\2\u00ec\u00ed\7q\2\2\u00ed\u00ee\7h\2\2\u00ee\u00ef\7h\2\2\u00ef"+
		"\u00f0\7u\2\2\u00f0\u00f1\7g\2\2\u00f1\u00f2\7v\2\2\u00f2\36\3\2\2\2\u00f3"+
		"\u00f4\7d\2\2\u00f4\u00f5\7{\2\2\u00f5 \3\2\2\2\u00f6\u00f7\7y\2\2\u00f7"+
		"\u00f8\7k\2\2\u00f8\u00f9\7v\2\2\u00f9\u00fa\7j\2\2\u00fa\u00fb\7q\2\2"+
		"\u00fb\u00fc\7w\2\2\u00fc\u00fd\7v\2\2\u00fd\"\3\2\2\2\u00fe\u00ff\7q"+
		"\2\2\u00ff\u0100\7p\2\2\u0100\u0101\3\2\2\2\u0101\u0102\6\22\2\2\u0102"+
		"$\3\2\2\2\u0103\u0104\7k\2\2\u0104\u0105\7i\2\2\u0105\u0106\7p\2\2\u0106"+
		"\u0107\7q\2\2\u0107\u0108\7t\2\2\u0108\u0109\7k\2\2\u0109\u010a\7p\2\2"+
		"\u010a\u010b\7i\2\2\u010b&\3\2\2\2\u010c\u010d\7i\2\2\u010d\u010e\7t\2"+
		"\2\u010e\u010f\7q\2\2\u010f\u0110\7w\2\2\u0110\u0111\7r\2\2\u0111\u0112"+
		"\7a\2\2\u0112\u0113\7n\2\2\u0113\u0114\7g\2\2\u0114\u0115\7h\2\2\u0115"+
		"\u0116\7v\2\2\u0116(\3\2\2\2\u0117\u0118\7i\2\2\u0118\u0119\7t\2\2\u0119"+
		"\u011a\7q\2\2\u011a\u011b\7w\2\2\u011b\u011c\7r\2\2\u011c\u011d\7a\2\2"+
		"\u011d\u011e\7t\2\2\u011e\u011f\7k\2\2\u011f\u0120\7i\2\2\u0120\u0121"+
		"\7j\2\2\u0121\u0122\7v\2\2\u0122*\3\2\2\2\u0123\u0124\7d\2\2\u0124\u0125"+
		"\7q\2\2\u0125\u0126\7q\2\2\u0126\u0127\7n\2\2\u0127,\3\2\2\2\u0128\u0129"+
		"\7.\2\2\u0129.\3\2\2\2\u012a\u012b\7?\2\2\u012b\u012c\6\30\3\2\u012c\60"+
		"\3\2\2\2\u012d\u012e\7<\2\2\u012e\62\3\2\2\2\u012f\u0130\7=\2\2\u0130"+
		"\64\3\2\2\2\u0131\u0132\7a\2\2\u0132\66\3\2\2\2\u0133\u0134\7z\2\2\u0134"+
		"8\3\2\2\2\u0135\u0136\7>\2\2\u0136\u0137\7u\2\2\u0137\u0138\7r\2\2\u0138"+
		"\u0139\7c\2\2\u0139\u013a\7e\2\2\u013a\u013b\7g\2\2\u013b\u013c\7@\2\2"+
		"\u013c:\3\2\2\2\u013d\u013e\t\4\2\2\u013e\u013f\t\5\2\2\u013f\u0140\t"+
		"\4\2\2\u0140\u0141\6\36\4\2\u0141<\3\2\2\2\u0142\u0143\t\6\2\2\u0143\u0144"+
		"\t\4\2\2\u0144\u0145\t\7\2\2\u0145\u0146\6\37\5\2\u0146>\3\2\2\2\u0147"+
		"\u0149\t\b\2\2\u0148\u0147\3\2\2\2\u0149\u014a\3\2\2\2\u014a\u0148\3\2"+
		"\2\2\u014a\u014b\3\2\2\2\u014b\u014f\3\2\2\2\u014c\u014e\t\t\2\2\u014d"+
		"\u014c\3\2\2\2\u014e\u0151\3\2\2\2\u014f\u014d\3\2\2\2\u014f\u0150\3\2"+
		"\2\2\u0150@\3\2\2\2\u0151\u014f\3\2\2\2\u0152\u0154\t\n\2\2\u0153\u0152"+
		"\3\2\2\2\u0154\u0157\3\2\2\2\u0155\u0153\3\2\2\2\u0155\u0156\3\2\2\2\u0156"+
		"\u0158\3\2\2\2\u0157\u0155\3\2\2\2\u0158\u015a\7<\2\2\u0159\u015b\t\13"+
		"\2\2\u015a\u0159\3\2\2\2\u015b\u015c\3\2\2\2\u015c\u015a\3\2\2\2\u015c"+
		"\u015d\3\2\2\2\u015d\u015e\3\2\2\2\u015e\u015f\6!\6\2\u015fB\3\2\2\2\u0160"+
		"\u0161\7*\2\2\u0161\u0162\b\"\4\2\u0162D\3\2\2\2\u0163\u0164\7+\2\2\u0164"+
		"\u0165\b#\5\2\u0165F\3\2\2\2\u0166\u0167\7}\2\2\u0167\u0168\b$\6\2\u0168"+
		"H\3\2\2\2\u0169\u016a\7\177\2\2\u016a\u016b\b%\7\2\u016bJ\3\2\2\2\u016c"+
		"\u016d\7]\2\2\u016d\u016e\b&\b\2\u016eL\3\2\2\2\u016f\u0170\7_\2\2\u0170"+
		"\u0171\b\'\t\2\u0171N\3\2\2\2\u0172\u0173\7?\2\2\u0173\u0176\7?\2\2\u0174"+
		"\u0176\7?\2\2\u0175\u0172\3\2\2\2\u0175\u0174\3\2\2\2\u0176P\3\2\2\2\u0177"+
		"\u0178\7?\2\2\u0178\u0179\7\u0080\2\2\u0179\u017a\3\2\2\2\u017a\u017b"+
		"\6)\7\2\u017bR\3\2\2\2\u017c\u017d\7#\2\2\u017d\u017e\7\u0080\2\2\u017e"+
		"\u017f\3\2\2\2\u017f\u0180\6*\b\2\u0180T\3\2\2\2\u0181\u0182\7#\2\2\u0182"+
		"\u0183\7?\2\2\u0183\u0184\3\2\2\2\u0184\u0185\6+\t\2\u0185V\3\2\2\2\u0186"+
		"\u0187\7>\2\2\u0187X\3\2\2\2\u0188\u0189\7@\2\2\u0189Z\3\2\2\2\u018a\u018b"+
		"\7@\2\2\u018b\u018c\7?\2\2\u018c\\\3\2\2\2\u018d\u018e\7>\2\2\u018e\u018f"+
		"\7?\2\2\u018f^\3\2\2\2\u0190\u0191\7-\2\2\u0191`\3\2\2\2\u0192\u0193\7"+
		"/\2\2\u0193b\3\2\2\2\u0194\u0195\7,\2\2\u0195d\3\2\2\2\u0196\u0197\7\61"+
		"\2\2\u0197f\3\2\2\2\u0198\u0199\7`\2\2\u0199h\3\2\2\2\u019a\u019b\7\'"+
		"\2\2\u019bj\3\2\2\2\u019c\u019d\7u\2\2\u019dl\3\2\2\2\u019e\u019f\7o\2"+
		"\2\u019fn\3\2\2\2\u01a0\u01a1\7j\2\2\u01a1p\3\2\2\2\u01a2\u01a3\7f\2\2"+
		"\u01a3r\3\2\2\2\u01a4\u01a5\7y\2\2\u01a5t\3\2\2\2\u01a6\u01a7\7{\2\2\u01a7"+
		"v\3\2\2\2\u01a8\u01aa\t\f\2\2\u01a9\u01a8\3\2\2\2\u01aa\u01ab\3\2\2\2"+
		"\u01ab\u01a9\3\2\2\2\u01ab\u01ac\3\2\2\2\u01acx\3\2\2\2\u01ad\u01ae\7"+
		"\17\2\2\u01ae\u01af\3\2\2\2\u01af\u01b0\b=\n\2\u01b0z\3\2\2\2\u01b1\u01b2"+
		"\7\f\2\2\u01b2\u01b3\3\2\2\2\u01b3\u01b4\b>\n\2\u01b4|\3\2\2\2\u01b5\u01b7"+
		"\7\13\2\2\u01b6\u01b5\3\2\2\2\u01b7\u01b8\3\2\2\2\u01b8\u01b6\3\2\2\2"+
		"\u01b8\u01b9\3\2\2\2\u01b9\u01ba\3\2\2\2\u01ba\u01bb\b?\n\2\u01bb~\3\2"+
		"\2\2\u01bc\u01be\7\"\2\2\u01bd\u01bc\3\2\2\2\u01be\u01bf\3\2\2\2\u01bf"+
		"\u01bd\3\2\2\2\u01bf\u01c0\3\2\2\2\u01c0\u01c1\3\2\2\2\u01c1\u01c2\b@"+
		"\n\2\u01c2\u0080\3\2\2\2\u01c3\u01c4\t\t\2\2\u01c4\u0082\3\2\2\2\u01c5"+
		"\u01c6\7\60\2\2\u01c6\u0084\3\2\2\2\23\2\u008b\u0091\u0099\u009e\u00a6"+
		"\u00ab\u00b1\u00b6\u014a\u014f\u0155\u015c\u0175\u01ab\u01b8\u01bf\13"+
		"\3\2\2\3\3\3\3\"\4\3#\5\3$\6\3%\7\3&\b\3\'\t\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}