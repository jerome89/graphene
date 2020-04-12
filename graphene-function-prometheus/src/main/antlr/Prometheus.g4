grammar Prometheus;

// Reference
// Negative lookahead : https://stackoverflow.com/questions/24194110/antlr4-negative-lookahead-in-lexer

/*------------------------------------------------------------------
 * LEXER Member
 *------------------------------------------------------------------*/

@lexer::members {
  boolean isBraceOpen = false;
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

LEFT_PAREN: '(';
RIGHT_PAREN: ')' {};
LEFT_BRACE: '{' {
  if (isBraceOpen) {
    throw new IllegalBraceException("This is not allowed action. Please check the duplicated left brace.");
  }
  isBraceOpen = true;
};
RIGHT_BRACE: '}' {
  if (!isBraceOpen) {
    throw new IllegalBraceException("This is not allowed action. Please check the left brace omitted.");
  }

  isBraceOpen = false;
};
LEFT_BRACKET: '[';
RIGHT_BRACKET: ']';
COMMA: ',';
ASSIGN: '=' { !isBraceOpen }?;
COLON: ':';
SEMICOLON: ';';
BLANK: '_';
TIMES: 'x';
SPACE: '<space>';
NAN: [nN][aA][nN] { !isBraceOpen }?;
INF: [iI][nN][fF];

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
