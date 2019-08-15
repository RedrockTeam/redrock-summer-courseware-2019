const ReadStream = require('stream').Readable;
ReadStream.prototype.pipe = function (dest) {
    this.on('data', (data)=>{
        let flag = dest.write(data);
        if(!flag){
            this.pause();
        }
    });
    dest.on('drain', ()=>{
        this.resume();
    });
    this.on('end', ()=>{
        dest.end();
    });
    dest.emit('pipe', this);
    return dest
}

const fs = require('fs')
const frs = fs.createReadStream('./files/file1.html');
const fws = fs.createWriteStream('./files/file2.html');
frs.pipe(fws)
