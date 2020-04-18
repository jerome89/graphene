grammar Prometheus;

// Reference
// Negative lookahead : https://stackoverflow.com/questions/24194110/antlr4-negative-lookahead-in-lexer

/*------------------------------------------------------------------
 * LEXER Member
 *------------------------------------------------------------------*/

@lexer::members {
  boolean isParenOpen = false;
  int isParenOpenCount = 0;

  boolean isBraceOpen = false;
  int isBraceOpenCount = 0;

  boolean isBracketOpen = false;
  int isBracketOpenCount = 0;

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
        if (type != NUMBER && type != EOF && nextChar != ' ') {
          if (type == DURATION_D || type == DURATION_H || type == DURATION_M || type == DURATION_S || type == DURATION_W || type == DURATION_Y) {
            // duration
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
METRIC_IDENTIFIER: [a-zA-Z]*':'[a-zA-Z0-9]+ { !isBracketOpen && 0 == isBracketOpenCount }?;

LEFT_PAREN: '(' {
  if (isParenOpen) {
    throw new IllegalParenException("This is not allowed action. Please check the duplicated left paren.");
  }

  isParenOpen = true;
  isParenOpenCount++;
};
RIGHT_PAREN: ')' {
  if (!isParenOpen) {
    throw new IllegalParenException("This is not allowed action. Please check the left paren omitted.");
  }

  isParenOpen = false;
};
LEFT_BRACE: '{' {
  if (isBraceOpen) {
    throw new IllegalBraceException("This is not allowed action. Please check the duplicated left brace.");
  }

  isBraceOpen = true;
  isBraceOpenCount++;
};
RIGHT_BRACE: '}' {
  if (!isBraceOpen) {
    throw new IllegalBraceException("This is not allowed action. Please check the left brace omitted.");
  }

  isBraceOpen = false;
};
LEFT_BRACKET: '[' {
  if (isBracketOpen) {
    throw new IllegalBracketException("This is not allowed action. Please check the duplicated left bracket.");
  }

  isBracketOpen = true;
  isBracketOpenCount++;
};
RIGHT_BRACKET: ']' {
  if (!isBracketOpen) {
    throw new IllegalBracketException("This is not allowed action. Please check the left bracket omitted.");
  }

  isBracketOpen = false;
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

// Duration Keyword
DURATION_S: 's';
DURATION_M: 'm';
DURATION_H: 'h';
DURATION_D: 'd';
DURATION_W: 'w';
DURATION_Y: 'y';

//=!#$%&:;<>?@[\]^_`|~
STRING: [A-Za-z\\"'.`:]+;

CR: '\r' -> skip;
NL: '\n' -> skip;
TAB: '\t'+ -> skip;
WS: ' '+ -> skip;

fragment
  DEGIT: [0-9];
  PT: '.';
