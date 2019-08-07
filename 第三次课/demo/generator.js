function* foo(x) {
  let y = x * (yield 3);
  return y;
}

const it = foo(6);

const firstRes = it.next();

console.log(firstRes);

const res = it.next(7);

console.log(res);
console.log(res.value);






function* foo() {
  let x = yield 2;
  z++;
  let y = yield x * z;
  console.log(x, y, z);
}

var z = 1;

var it1 = foo();
var it2 = foo();

var val1 = it1.next().value;
var val2 = it2.next().value;

val1 = it1.next(val2 * 10).value;
val2 = it2.next(val1 * 5).value;

it1.next(val2 / 2);
it2.next(val1 / 4);
