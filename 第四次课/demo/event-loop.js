setTimeout(() => {
  console.log(1);
}, 0)
setImmediate(() => {
    console.log(2)
});
process.nextTick(() => {
    console.log(3)
})
