grammar Prometheus;

// Reference
// Negative lookahead : https://stackoverflow.com/questions/24194110/antlr4-negative-lookahead-in-lexer

/*------------------------------------------------------------------
 * LEXER Member
 *------------------------------------------------------------------*/

@lexer::members {
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
}

/*------------------------------------------------------------------
* PARSER RULES
*------------------------------------------------------------------*/

start: duration;

duration: LEFT_BRACE DURATION RIGHT_BRACE;

/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

COMMENT: '#' {
  int i = 1;
  int type = _input.LA(i);
  while(type != EOF) {
    i++;
    type = _input.LA(i);
  }
  Token o = _factory.create(_tokenFactorySourcePair, COMMENT, _text, _channel, _tokenStartCharIndex, i - 1, _tokenStartLine, _tokenStartCharPositionInLine);
  emit(o);
  _input.seek(i);
};

NUMBER: DEGIT+ PT DEGIT+
      | '0x'DEGIT+        // hexadecimal
      | DEGIT+ PT
      | PT DEGIT+
      | DEGIT+ {
        if (isBraceOpen && !isBracketOpen) {
          throw new UnexpectedCharInsideBraceException("Numbers are only available for duration");
        }

        int type = _input.LA(1);
        _input.LA(-1);

        char nextChar = (char) type;
        boolean isNumber = 48 <= type && 58 > type;
        if (!isNumber && type != EOF && nextChar != ' ') {
          char[] chars = "smhdwy".toCharArray();

          boolean includeDurationKeyword = false;
          for (char aChar : chars) {
            if (Character.toLowerCase(nextChar) == aChar) {
              includeDurationKeyword = true;
              break;
            }
          }

          if (includeDurationKeyword) {
            Token o = _factory.create(_tokenFactorySourcePair, DURATION, _text, _channel, _tokenStartCharIndex, getCharIndex(), _tokenStartLine, _tokenStartCharPositionInLine);
            emit(o);
            _input.seek(getCharIndex() + 1);
          } else {
            throw new BadNumberOrDurationException("bad number or duration syntax");
          }
        }
      }
      | NAN
      | INF
      ;

DURATION: [1-9]+[smhdwy];

// Condition
LAND: 'AND';
LOR: 'or';
LUNLESS: 'unless';

// Aggregator
SUM: 'sum';
AVG: 'AVG';
MAX: 'MAX';
MIN: 'min';
COUNT: 'count';
STDVAR: 'stdvar';
STDDEV: 'stddev';

// Keywords
OFFSET: 'offset';
BY: 'by';
WITHOUT: 'without';
ON: 'on' { !isBraceOpen }?;
IGNORING: 'ignoring';
GROUP_LEFT: 'group_left';
GROUP_RIGHT: 'group_right';
BOOL: 'bool';

COMMA: ',';
ASSIGN: '=' { useAssignmentStatement = true && !isBraceOpen }?;
COLON: ':';
SEMICOLON: ';';
BLANK: '_';
TIMES: 'x';
SPACE: '<space>';
NAN: [nN][aA][nN] { !isBraceOpen }?;
INF: [iI][nN][fF] { !isBraceOpen }?;

IDENTIFIER: [a-zA-Z_]+[0-9]*;
METRIC_IDENTIFIER: [a-zA-Z]*':'[a-zA-Z0-9]+ { !isBracketOpen && 0 == bracketOpenCount }?;

LEFT_PAREN: '(' {
  isParenOpen = true;
  parenOpenCount++;
};
RIGHT_PAREN: ')' {
  isParenOpen = false;
  parenCloseCount++;
};
LEFT_BRACE: '{' {
  if (isBraceOpen) {
    throw new IllegalBraceException("This is not allowed action. Please check the duplicated left brace.");
  }

  isBraceOpen = true;
  braceOpenCount++;
};
RIGHT_BRACE: '}' {
  isBraceOpen = false;
  braceCloseCount++;
};
LEFT_BRACKET: '[' {
  if (isBracketOpen) {
    throw new IllegalBracketException("This is not allowed action. Please check the duplicated left bracket.");
  }

  isBracketOpen = true;
  bracketOpenCount++;
};
RIGHT_BRACKET: ']' {
  isBracketOpen = false;
  bracketCloseCount++;
};

// Query Operator
EQL: '=='
  | '=';
EQL_REGEX: '=~' { useAssignmentStatement = true }?;
NEQ_REGEX: '!~' { useAssignmentStatement = true }?;
NEQ: '!=' { useAssignmentStatement = true }?;

LSS: '<';
GTR: '>';
GTE: '>=';
LTE: '<=';

// Operator
ADD: '+';
SUB: '-';
MUL: '*';
DIV: '/';
POW: '^';
MOD: '%';


//=!#$%&:;<>?@[\]^_`|~
STRING: [A-Za-z\\"'.`:]+;

CR: '\r' -> skip;
NL: '\n' -> skip;
TAB: '\t'+ -> skip;
WS: ' '+ -> skip;

fragment
  DEGIT: [0-9];
  PT: '.';
