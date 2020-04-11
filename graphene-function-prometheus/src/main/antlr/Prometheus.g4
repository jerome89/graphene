grammar Prometheus;

// Reference
// Negative lookahead : https://stackoverflow.com/questions/24194110/antlr4-negative-lookahead-in-lexer

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

IDENTIFIER: [a-zA-Z1-9]+;
METRIC_IDENTIFIER: [a-zA-Z1-9]*':'[a-zA-Z1-9]+;

LEFT_PAREN: '(';
RIGHT_PAREN: ')';
LEFT_BRACE: '{';
RIGHT_BRACE: '}';
LEFT_BRACKET: '[';
RIGHT_BRACKET: ']';
COMMA: ',';
ASSIGN: '=';
COLON: ':';
SEMICOLON: ';';
BLANK: '_';
TIMES: 'x';
SPACE: '<space>';
NAN: [nN][aA][nN];
INF: [iI][nN][fF];

EQL: '==';
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
