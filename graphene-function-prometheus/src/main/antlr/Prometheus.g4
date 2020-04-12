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
}

/*------------------------------------------------------------------
* PARSER RULES
*------------------------------------------------------------------*/

start: duration;

duration: LEFT_BRACE DURATION RIGHT_BRACE;

/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

NUMBER: DEGIT+ PT DEGIT+
      | '0x'DEGIT+        // hexadecimal
      | DEGIT+ PT
      | PT DEGIT+
      | DEGIT+
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

IDENTIFIER: [a-zA-Z1-9]+;
METRIC_IDENTIFIER: [a-zA-Z1-9]*':'[a-zA-Z1-9]+;

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
COMMA: ',';
ASSIGN: '=' { !isBraceOpen }?;
COLON: ':';
SEMICOLON: ';';
BLANK: '_';
TIMES: 'x';
SPACE: '<space>';
NAN: [nN][aA][nN] { !isBraceOpen }?;
INF: [iI][nN][fF];

// Query Operator
EQL: '=='
  | '=';
EQL_REGEX: '=~';
NEQ_REGEX: '!~';
NEQ: '!=';

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
STRING: [A-Za-z\\"'.`]+;
COMMENT: '#'[ A-Za-z1-9+]+;

fragment
  DEGIT: [0-9];
  PT: '.';
