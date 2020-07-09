# openssl教程

### 1. 标准命令

| Standard commands | 作用                         |
| ----------------- | ---------------------------- |
| genrsa            | 生成RSA私钥                  |
| rsa               | RSA公私钥相关操作(属于pkcs1) |
| req               | CSR                          |
| ca                | CA认证操作                   |
| x509              | 证书操作                     |
| pkcs8             | pkcs8格式操作                |

### 2. genrsa

```bash
# 命令帮助
usage: genrsa [args] [numbits]
 -des            encrypt the generated key with DES in cbc mode
 -des3           encrypt the generated key with DES in ede cbc mode (168 bit key)
 -idea           encrypt the generated key with IDEA in cbc mode
 -seed
                 encrypt PEM output with cbc seed
 -aes128, -aes192, -aes256
                 encrypt PEM output with cbc aes
 -camellia128, -camellia192, -camellia256
                 encrypt PEM output with cbc camellia
 -out file       output the key to 'file
 -passout arg    output file pass phrase source
 -f4             use F4 (0x10001) for the E value
 -3              use 3 for the E value
 -engine e       use engine e, possibly a hardware device.
 -rand file:file:...
                 load the file (or the files in the directory) into
                 the random number generator
##############################################################################

# 生成RSA，私钥长度1024
openssl genrsa 1024
# 生成RSA，私钥长度1024，结果保存至文件private.pem
openssl genrsa  -out private.pem 1024
# 生成RSA，私钥长度2048，私钥进行des3加密,结果保存至文件private.pem
openssl genrsa -des3 -out private.pem 2048
# 生成RSA，私钥长度2048，私钥进行des加密,密码在password.txt中,结果保存至文件private.pem
openssl genrsa -des -passout file:password.txt -out private.pem 2048
# 生成RSA，私钥长度2048，私钥进行des加密,密码为文字:1234,结果保存至文件private.pem
openssl genrsa -des -passout pass:1234 -out private.pem 2048
```

### 3. rsa

```bash
# 命令帮助
rsa [options] <infile >outfile
where options are
 -inform arg     input format - one of DER NET PEM
 -outform arg    output format - one of DER NET PEM
 -in arg         input file
 -sgckey         Use IIS SGC key format
 -passin arg     input file pass phrase source
 -out arg        output file
 -passout arg    output file pass phrase source
 -des            encrypt PEM output with cbc des
 -des3           encrypt PEM output with ede cbc des using 168 bit key
 -idea           encrypt PEM output with cbc idea
 -seed           encrypt PEM output with cbc seed
 -aes128, -aes192, -aes256
                 encrypt PEM output with cbc aes
 -camellia128, -camellia192, -camellia256
                 encrypt PEM output with cbc camellia
 -text           print the key in text
 -noout          don't print key out
 -modulus        print the RSA key modulus
 -check          verify key consistency
 -pubin          expect a public key in input file
 -pubout         output a public key
 -engine e       use engine e, possibly a hardware device.
##############################################################################

# 查看私钥内容
openssl rsa -in private.pem
# 查看私钥内容,打印key
openssl rsa -in private.pem -text

# 将PEM格式转为DER
openssl rsa -in private.pem  -inform PEM -outform DER -out private.der
# 将DER格式转为PEM
openssl rsa -in private.der -inform DER -outform PEM -out private4.pem

# 将私钥加密，保存至private2.pem
openssl rsa -in private.pem  -des -passout pass:1234 -out private2.pem
# 将私钥解密，保存至private3.pem
openssl rsa -in private2.pem -passin pass:1234 -out private3.pem

# 从私钥从获取公钥
openssl rsa -in private.pem -pubout -out pub.pem
# 查看公钥内容
openssl rsa -pubin -in pub.pem

# 检查私钥完整性
openssl rsa -in private.pem -check
```

### 4. pkcs1（rsa）和pkcs8互转

| 源类型 | 源钥 | 目标类型 | 目标钥 | 命令                                                         |
| ------ | ---- | -------- | ------ | ------------------------------------------------------------ |
|        |      | pkcs1    | 私钥   | openssl genrsa -out private.pkcs1 1024                       |
|        |      |          |        |                                                              |
| pkcs1  | 私钥 | pkcs1    | 公钥   | openssl rsa -in private.pkcs1 -RSAPublicKey_out -out public.pkcs1 |
| pkcs1  | 私钥 | pkcs8    | 公钥   | openssl rsa -in private.pkcs1 -pubout -out public.pkcs8      |
| pkcs1  | 私钥 | pkcs8    | 私钥   | openssl pkcs8 -topk8 -in private.pkcs1 -nocrypt -out private.pkcs8 |
| pkcs8  | 私钥 | pkcs1    | 私钥   | openssl rsa -in private.pkcs8 -out private_restore.pkcs1     |
| pkcs8  | 私钥 | pkcs1    | 公钥   | openssl rsa -in private.pkcs8 -RSAPublicKey_out -out public.pkcs1 |
| pkcs8  | 私钥 | pkcs8    | 公钥   | openssl rsa -in private.pkcs8 -pubout -out public.pkcs8      |
|        |      |          |        |                                                              |
| pkcs8  | 公钥 | pkcs1    | 公钥   | openssl rsa -pubin -in public.pkcs8  -RSAPublicKey_out -out public.pkcs1 |
| pkcs1  | 公钥 | pkcs8    | 公钥   | openssl rsa -RSAPublicKey_in -in public.pkcs1  -pubout -out public.pkcs8 |


