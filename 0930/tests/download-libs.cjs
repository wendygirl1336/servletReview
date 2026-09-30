const fs = require('fs');
const path = require('path');
const https = require('https');
const destination = process.argv[2];
fs.mkdirSync(destination, { recursive: true });
const dependencies = {
  'tomcat-core': 'org/apache/tomcat/embed/tomcat-embed-core/10.1.48/tomcat-embed-core-10.1.48.jar',
  jasper: 'org/apache/tomcat/embed/tomcat-embed-jasper/10.1.48/tomcat-embed-jasper-10.1.48.jar',
  el: 'org/apache/tomcat/embed/tomcat-embed-el/10.1.48/tomcat-embed-el-10.1.48.jar',
  ecj: 'org/eclipse/jdt/ecj/3.33.0/ecj-3.33.0.jar',
  annotation: 'jakarta/annotation/jakarta.annotation-api/2.1.1/jakarta.annotation-api-2.1.1.jar',
  ant: 'org/apache/ant/ant/1.10.15/ant-1.10.15.jar',
  'ant-launcher': 'org/apache/ant/ant-launcher/1.10.15/ant-launcher-1.10.15.jar'
};
Promise.all(Object.entries(dependencies).map(([name, location]) => new Promise((resolve, reject) => {
  const file = path.join(destination, name + '.jar');
  const temporary = file + '.part';
  if (fs.existsSync(file)) return resolve();
  https.get('https://repo.maven.apache.org/maven2/' + location, response => {
    if (response.statusCode !== 200) {
      response.resume();
      return reject(new Error('Download failed: ' + name + ' HTTP ' + response.statusCode));
    }
    const output = fs.createWriteStream(temporary);
    response.on('error', reject);
    output.on('error', reject);
    output.on('finish', () => output.close(() => {
      fs.renameSync(temporary, file);
      resolve();
    }));
    response.pipe(output);
  }).on('error', reject);
}))).catch(error => { console.error(error.message); process.exitCode = 1; });
