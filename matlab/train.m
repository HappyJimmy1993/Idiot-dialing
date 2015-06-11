function code = train(traindir, n)  %己改，n 为几就是训练第几个
                             %code为已训练好的码书；训练时同时用到了mfcc.m和vqlb.m;
% Speaker Recognition: Training Stage %训练阶段
%
% Input:
%       traindir : string name of directory contains all train sound files
%                  测试声音文件名
%       n        : number of train files in traindir （testdir中测试文件数)
%
% Output:
%       code     : trained VQ codebooks, code{i} for i-th speaker
%
% Note:
%       Sound files in traindir is supposed to be: 
%                       s1.wav, s2.wav, ..., sn.wav
% Example:
%       >> code = train('C:\data\train\', 8);

k = 16;                         % number of centroids required

 for i = 1:n                      % train a VQ codebook for each speaker
         
file=sprintf('%s%d.wav', traindir,i);   %%s%d.wav这是什么？？？？？？？？？？？？sprintf完成的是什么功能？？？？？？？？？这里有改动；\\\\\
disp(file);
    % 语音存储路径显示
      
    [s, fs] = wavread(file);
    
    v = mfcc(s, fs);            % Compute MFCC's
   
    code{i} = vqlbg(v, k);      % Train VQ codebook生成码书；这里该过、、、、、、、
 end
