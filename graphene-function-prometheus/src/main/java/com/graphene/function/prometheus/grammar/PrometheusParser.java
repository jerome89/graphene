// Generated from Prometheus.g4 by ANTLR 4.7.2
package com.graphene.function.prometheus.grammar;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PrometheusParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		NUMBER=1, DURATION=2, LAND=3, LOR=4, LUNLESS=5, SUM=6, AVG=7, MAX=8, MIN=9, 
		COUNT=10, STDVAR=11, STDDEV=12, IDENTIFIER=13, METRIC_IDENTIFIER=14, LEFT_PAREN=15, 
		RIGHT_PAREN=16, LEFT_BRACE=17, RIGHT_BRACE=18, LEFT_BRACKET=19, RIGHT_BRACKET=20, 
		COMMA=21, ASSIGN=22, COLON=23, SEMICOLON=24, BLANK=25, TIMES=26, SPACE=27, 
		NAN=28, INF=29, EQL=30, NEQ=31, LSS=32, GTR=33, GTE=34, LTE=35, ADD=36, 
		SUB=37, MUL=38, DIV=39, POW=40, MOD=41, STRING=42, COMMENT=43, PT=44;
	public static final int
		RULE_start = 0, RULE_duration = 1;
	private static String[] makeRuleNames() {
		return new String[] {
			"start", "duration"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'AND'", "'or'", "'unless'", "'sum'", "'AVG'", "'MAX'", 
			"'min'", "'count'", "'stdvar'", "'stddev'", null, null, "'('", "')'", 
			"'{'", "'}'", "'['", "']'", "','", "'='", "':'", "';'", "'_'", "'x'", 
			"'<space>'", null, null, "'=='", "'!='", "'<'", "'>'", "'>='", "'<='", 
			"'+'", "'-'", "'*'", "'/'", "'^'", "'%'", null, null, "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "NUMBER", "DURATION", "LAND", "LOR", "LUNLESS", "SUM", "AVG", "MAX", 
			"MIN", "COUNT", "STDVAR", "STDDEV", "IDENTIFIER", "METRIC_IDENTIFIER", 
			"LEFT_PAREN", "RIGHT_PAREN", "LEFT_BRACE", "RIGHT_BRACE", "LEFT_BRACKET", 
			"RIGHT_BRACKET", "COMMA", "ASSIGN", "COLON", "SEMICOLON", "BLANK", "TIMES", 
			"SPACE", "NAN", "INF", "EQL", "NEQ", "LSS", "GTR", "GTE", "LTE", "ADD", 
			"SUB", "MUL", "DIV", "POW", "MOD", "STRING", "COMMENT", "PT"
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

	@Override
	public String getGrammarFileName() { return "Prometheus.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public PrometheusParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class StartContext extends ParserRuleContext {
		public DurationContext duration() {
			return getRuleContext(DurationContext.class,0);
		}
		public StartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_start; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrometheusListener ) ((PrometheusListener)listener).enterStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrometheusListener ) ((PrometheusListener)listener).exitStart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrometheusVisitor ) return ((PrometheusVisitor<? extends T>)visitor).visitStart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StartContext start() throws RecognitionException {
		StartContext _localctx = new StartContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_start);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(4);
			duration();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DurationContext extends ParserRuleContext {
		public TerminalNode LEFT_BRACE() { return getToken(PrometheusParser.LEFT_BRACE, 0); }
		public TerminalNode DURATION() { return getToken(PrometheusParser.DURATION, 0); }
		public TerminalNode RIGHT_BRACE() { return getToken(PrometheusParser.RIGHT_BRACE, 0); }
		public DurationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_duration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PrometheusListener ) ((PrometheusListener)listener).enterDuration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PrometheusListener ) ((PrometheusListener)listener).exitDuration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PrometheusVisitor ) return ((PrometheusVisitor<? extends T>)visitor).visitDuration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DurationContext duration() throws RecognitionException {
		DurationContext _localctx = new DurationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_duration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(6);
			match(LEFT_BRACE);
			setState(7);
			match(DURATION);
			setState(8);
			match(RIGHT_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3.\r\4\2\t\2\4\3\t"+
		"\3\3\2\3\2\3\3\3\3\3\3\3\3\3\3\2\2\4\2\4\2\2\2\n\2\6\3\2\2\2\4\b\3\2\2"+
		"\2\6\7\5\4\3\2\7\3\3\2\2\2\b\t\7\23\2\2\t\n\7\4\2\2\n\13\7\24\2\2\13\5"+
		"\3\2\2\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}