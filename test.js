const assert = require('assert');

describe('Addition', function() {
  it('2 + 2 should be 4', function() {
    assert.equal((2 + 2), 4);
  });

  it('1 + 5 should be 6', function() {
    assert.equal((1 + 5), 6);
  });
});

describe('Type Comparison', function() {
  it('\'5\' == 5 should be true', function() {
    assert.equal(('5' == 5), true);
  });
  
  it('\'5\' === 5 should be false', function() {
    assert.equal(('5' === 5), false);
  });
});
