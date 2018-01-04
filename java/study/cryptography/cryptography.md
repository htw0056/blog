# 密码学

## 1. 密码学分类

1. 消息编码：
   - Base64  
2. 消息摘要：唯一对应一个消息或文本的固定长度的值，由一个单向Hash加密函数对消息进行作用而产生。
   - MD(Message Digest):消息摘要算法,为计算机安全领域广泛使用的一种散列函数，用以提供消息的**完整性**保护
   - SHA(Secure Hash Algorithm):安全散列算法，主要适用于数字签名标准里面定义的数字签名算法
   - MAC(Message Authentication Code):消息认证码算法，结合了MD5和SHA算法的优势，同时用**秘钥**对摘要加密，是一种更为安全的消息摘要算法
3. 对称密码：
   - DES(Data Encrytion Standard)  
   - 3DES(Triple DES,DESede)  
   - AES(Advanced Encryption Standard)
4. 非对称密码：
   - RSA
   - DH密钥交换  
5. 数字签名：数字签名是带有密钥(公钥和私钥)的消息摘要算法，是非对称加密算法和消息摘要算法结合体
   - RSASignature
   - DSASignature  


## 2. 密码学五元组

(明文，密文，加密算法，解密算法，密钥）

## 3. Java编程中常用类

1. 消息编码

    BASE64Encoder,BASE64Decoder

2. 消息摘要

    MessageDigest

3. 对称密码

    KeyGenerator,SecretKey,Cipher

4. 非对称密码

    KeyPairGenerator,KeyFactory,Keypair,PublicKey,PrivateKey,Cipher

5. 数字签名

    Signature