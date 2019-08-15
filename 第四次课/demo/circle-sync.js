const fs = require('fs')
for(let i = 1; i < 5; i++) {
    console.log(i, fs.statSync(`./files/file${i}.html`).ctime);
}
console.log('a');
