function varargout = jh(varargin)
% JH MATLAB code for jh.fig
%      JH, by itself, creates a new JH or raises the existing
%      singleton*.
%
%      H = JH returns the handle to a new JH or the handle to
%      the existing singleton*.
%
%      JH('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in JH.M with the given input arguments.
%
%      JH('Property','Value',...) creates a new JH or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before jh_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to jh_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help jh

% Last Modified by GUIDE v2.5 18-Oct-2014 00:31:52

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @jh_OpeningFcn, ...
                   'gui_OutputFcn',  @jh_OutputFcn, ...
                   'gui_LayoutFcn',  [] , ...
                   'gui_Callback',   []);
if nargin && ischar(varargin{1})
    gui_State.gui_Callback = str2func(varargin{1});
end

if nargout
    [varargout{1:nargout}] = gui_mainfcn(gui_State, varargin{:});
else
    gui_mainfcn(gui_State, varargin{:});
end
% End initialization code - DO NOT EDIT


% --- Executes just before jh is made visible.
function jh_OpeningFcn(hObject, eventdata, handles, varargin)
global s1 s2 s3 s4 s5 s6 s7 s8 bz1 bz2 bz3 bz4 bz5 bz6 bz7 bz8 az1 az2 az3 az4 az5 az6 az7 az8;
s1=1;
s2=1;
s3=1;
s4=1;
s5=1;
s6=1;
s7=1;
s8=1;
set(handles.slider1,'value',0);
set(handles.slider2,'value',0);
set(handles.slider3,'value',0);
set(handles.slider4,'value',0);
set(handles.slider5,'value',0);
set(handles.slider6,'value',0);
set(handles.slider7,'value',0);
set(handles.slider8,'value',0);

Fs1=32000;Fp1=10;Ft1=100;
wp1=2*pi*Fp1/Fs1; wt1=2*pi*Ft1/Fs1; 
wp11=2*Fs1*tan(wp1/2); wt11=2*Fs1*tan(wt1/2);
[N1,Wn1]=buttord(wp11,wt11,1,20,'s'); 
[Z1,P1,K1]=buttap(N1); 
[Bap1,Aap1]=zp2tf(Z1,P1,K1); 
[b1,a1]=lp2lp(Bap1,Aap1,Wn1); 
[bz1,az1]=bilinear(b1,a1,Fs1); 
[H1,W1]=freqz(bz1,az1); 
%figure(1);
%plot(W1*Fs1/(2*pi),abs(H1)*s1);
 
fp21=150;fp22=550;fs21=260;fs22=440;Fs2=32000;Ap2=1;As2=15;
wp21=2*pi*fp21/Fs2;wp22=2*pi*fp22/Fs2;
ws21=2*pi*fs21/Fs2;ws22=2*pi*fs22/Fs2; Ts2=1/Fs2;
wap21=2/Ts2*tan(wp21/2);wap22=2/Ts2*tan(wp22/2);
was21=2/Ts2*tan(ws21/2);was22=2/Ts2*tan(ws22/2);
wap2=[wap21,wap22];was2=[was21,was22];
W21=(wap21+was21)/2;W22=(wap22+was22)/2;
Bw2=W22-W21;
wo2=sqrt(W21*W22);
[N2,wc2]=buttord(wap2,was2,Ap2,As2,'s');
[z2,p2,k2]=buttap(N2);
[Bap2,Aap2]=zp2tf(z2,p2,k2);
[b2,a2]=lp2bp(Bap2,Aap2,wo2,Bw2);
[bz2,az2]=bilinear(b2,a2,Fs2);
[H2,w2]=freqz(bz2,az2);
%figure(2);
%plot(w2*Fs2/(2*pi),(abs(H2))*s2);
 
fp31=400;fp32=1100;fs31=550;fs32=950;Fs3=32000;Ap3=1;As3=15;
wp31=2*pi*fp31/Fs3;wp32=2*pi*fp32/Fs3;
ws31=2*pi*fs31/Fs3;ws32=2*pi*fs32/Fs3; Ts3=1/Fs3;
wap31=2/Ts3*tan(wp31/2);wap32=2/Ts3*tan(wp32/2);
was31=2/Ts3*tan(ws31/2);was32=2/Ts3*tan(ws32/2);
wap3=[wap31,wap32];was3=[was31,was32];
W31=(wap31+was31)/2;W32=(wap32+was32)/2;
Bw3=W32-W31;
wo3=sqrt(W31*W32);
[N3,wc3]=buttord(wap3,was3,Ap3,As3,'s');
[z3,p3,k3]=buttap(N3);
[Bap3,Aap3]=zp2tf(z3,p3,k3);
[b3,a3]=lp2bp(Bap3,Aap3,wo3,Bw3);
[bz3,az3]=bilinear(b3,a3,Fs3);
[H3,w3]=freqz(bz3,az3);
%figure(3);
%plot(w3*Fs3/(2*pi),(abs(H3))*s3);


fp41=800;fp42=2200;fs41=1100;fs42=1900;Fs4=32000;Ap4=1;As4=15;
wp41=2*pi*fp41/Fs4;wp42=2*pi*fp42/Fs4;
ws41=2*pi*fs41/Fs4;ws42=2*pi*fs42/Fs4; Ts4=1/Fs4;
wap41=2/Ts4*tan(wp41/2);wap42=2/Ts4*tan(wp42/2);
was41=2/Ts4*tan(ws41/2);was42=2/Ts4*tan(ws42/2);
wap4=[wap41,wap42];was4=[was41,was42];
W41=(wap41+was41)/2;W42=(wap42+was42)/2;
Bw4=W42-W41;
wo4=sqrt(W41*W42);
[N4,wc4]=buttord(wap4,was4,Ap4,As4,'s');
[z4,p4,k4]=buttap(N4);
[Bap4,Aap4]=zp2tf(z4,p4,k4);
[b4,a4]=lp2bp(Bap4,Aap4,wo4,Bw4);
[bz4,az4]=bilinear(b4,a4,Fs4);
[H4,w4]=freqz(bz4,az4);


fp51=1800;fp52=4200;fs51=2200;fs52=3800;Fs5=32000;Ap5=1;As5=15;
wp51=2*pi*fp51/Fs5;wp52=2*pi*fp52/Fs5;
ws51=2*pi*fs51/Fs5;ws52=2*pi*fs52/Fs5; Ts5=1/Fs5;
wap51=2/Ts5*tan(wp51/2);wap52=2/Ts5*tan(wp52/2);
was51=2/Ts5*tan(ws51/2);was52=2/Ts5*tan(ws52/2);
wap5=[wap51,wap52];was5=[was51,was52];
W51=(wap51+was51)/2;W52=(wap52+was52)/2;
Bw5=W52-W51;
wo5=sqrt(W51*W52);
[N5,wc5]=buttord(wap5,was5,Ap5,As5,'s');
[z5,p5,k5]=buttap(N5);
[Bap5,Aap5]=zp2tf(z5,p5,k5);
[b5,a5]=lp2bp(Bap5,Aap5,wo5,Bw5);
[bz5,az5]=bilinear(b5,a5,Fs5);
[H5,w5]=freqz(bz5,az5);


fp61=3800;fp62=8200;fs61=4200;fs62=7800;Fs6=32000;Ap6=1;As6=15;
wp61=2*pi*fp61/Fs6;wp62=2*pi*fp62/Fs6;
ws61=2*pi*fs61/Fs6;ws62=2*pi*fs62/Fs6; Ts6=1/Fs6;
wap61=2/Ts6*tan(wp61/2);wap62=2/Ts6*tan(wp62/2);
was61=2/Ts6*tan(ws61/2);was62=2/Ts6*tan(ws62/2);
wap6=[wap61,wap62];was6=[was61,was62];
W61=(wap61+was61)/2;W62=(wap62+was62)/2;
Bw6=W62-W61;
wo6=sqrt(W61*W62);
[N6,wc6]=buttord(wap6,was6,Ap6,As6,'s');
[z6,p6,k6]=buttap(N6);
[Bap6,Aap6]=zp2tf(z6,p6,k6);
[b6,a6]=lp2bp(Bap6,Aap6,wo6,Bw6);
[bz6,az6]=bilinear(b6,a6,Fs6);
[H6,w6]=freqz(bz6,az6);


fp71=7200;fp72=15800;fs71=8200;fs72=14800;Fs7=32000;Ap7=1;As7=15;
wp71=2*pi*fp71/Fs7;wp72=2*pi*fp72/Fs7;
ws71=2*pi*fs71/Fs7;ws72=2*pi*fs72/Fs7; Ts7=1/Fs7;
wap71=2/Ts7*tan(wp71/2);wap72=2/Ts7*tan(wp72/2);
was71=2/Ts7*tan(ws71/2);was72=2/Ts7*tan(ws72/2);
wap7=[wap71,wap72];was7=[was71,was72];
W71=(wap71+was71)/2;W72=(wap72+was72)/2;
Bw7=W72-W71;
wo7=sqrt(W71*W72);
[N7,wc7]=buttord(wap7,was7,Ap7,As7,'s');
[z7,p7,k7]=buttap(N7);
[Bap7,Aap7]=zp2tf(z7,p7,k7);
[b7,a7]=lp2bp(Bap7,Aap7,wo7,Bw7);
[bz7,az7]=bilinear(b7,a7,Fs7);
[H7,w7]=freqz(bz7,az7);


fp81=10;fp82=260;fs81=100;fs82=160;Fs8=32000;Ap8=1;As8=15;
wp81=2*pi*fp81/Fs8;wp82=2*pi*fp82/Fs8;
ws81=2*pi*fs81/Fs8;ws82=2*pi*fs82/Fs8; Ts8=1/Fs8;
wap81=2/Ts8*tan(wp81/2);wap82=2/Ts8*tan(wp82/2);
was81=2/Ts8*tan(ws81/2);was82=2/Ts8*tan(ws82/2);
wap8=[wap81,wap82];was8=[was81,was82];
W81=(wap81+was81)/2;W82=(wap82+was82)/2;
Bw8=W82-W81;
wo8=sqrt(W81*W82);
[N8,wc8]=buttord(wap8,was8,Ap8,As8,'s');
[z8,p8,k8]=buttap(N8);
[Bap8,Aap8]=zp2tf(z8,p8,k8);
[b8,a8]=lp2bp(Bap8,Aap8,wo8,Bw8);
[bz8,az8]=bilinear(b8,a8,Fs8);
[H8,w8]=freqz(bz8,az8);
%figure(8);
%plot(w8*Fs8/(2*pi),(abs(H8))*s8);


% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to jh (see VARARGIN)

% Choose default command line output for jh
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);

% UIWAIT makes jh wait for user response (see UIRESUME)
% uiwait(handles.figure1);


% --- Outputs from this function are returned to the command line.
function varargout = jh_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;
global n;   % 训练时记录的第几个话说人
global m;   % 测试时记录的第几个话说人
n=1;
m=1;

% --- Executes on slider movement.
function slider1_Callback(hObject, eventdata, handles)
global f1 s1 f11;
set(handles.text1,'string',num2str(get(hObject,'value')));
s1=get(hObject,'value');
s1=2.7183^(s1/10);
f11=s1*f1;




% hObject    handle to slider1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'Value') returns position of slider
%        get(hObject,'Min') and get(hObject,'Max') to determine range of slider


% --- Executes during object creation, after setting all properties.
function slider1_CreateFcn(hObject, eventdata, handles)
% hObject    handle to slider1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: slider controls usually have a light gray background.
if isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor',[.9 .9 .9]);
end


% --- Executes on slider movement.
function slider2_Callback(hObject, eventdata, handles)
global f2 s2 f22;
set(handles.text2,'string',num2str(get(hObject,'value')));
s2=get(hObject,'value');
s2=2.7183^(s2/10);
f22=s2*f2;

% hObject    handle to slider2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'Value') returns position of slider
%        get(hObject,'Min') and get(hObject,'Max') to determine range of slider


% --- Executes during object creation, after setting all properties.
function slider2_CreateFcn(hObject, eventdata, handles)
% hObject    handle to slider2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: slider controls usually have a light gray background.
if isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor',[.9 .9 .9]);
end


% --- Executes on slider movement.
function slider3_Callback(hObject, eventdata, handles)
global f3 s3 f33;
set(handles.text3,'string',num2str(get(hObject,'value')));
s3=get(hObject,'value');
s3=2.7183^(s3/10);
f33=s3*f3;
% hObject    handle to slider3 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'Value') returns position of slider
%        get(hObject,'Min') and get(hObject,'Max') to determine range of slider


% --- Executes during object creation, after setting all properties.
function slider3_CreateFcn(hObject, eventdata, handles)
% hObject    handle to slider3 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: slider controls usually have a light gray background.
if isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor',[.9 .9 .9]);
end


% --- Executes on slider movement.
function slider4_Callback(hObject, eventdata, handles)
global f4 s4 f44;
set(handles.text4,'string',num2str(get(hObject,'value')));
s4=get(hObject,'value');
s4=2.7183^(s4/10);
f44=s4*f4;
% hObject    handle to slider4 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'Value') returns position of slider
%        get(hObject,'Min') and get(hObject,'Max') to determine range of slider


% --- Executes during object creation, after setting all properties.
function slider4_CreateFcn(hObject, eventdata, handles)
% hObject    handle to slider4 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: slider controls usually have a light gray background.
if isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor',[.9 .9 .9]);
end


% --- Executes on slider movement.
function slider5_Callback(hObject, eventdata, handles)
global f5 s5 f55;
set(handles.text5,'string',num2str(get(hObject,'value')));
s5=get(hObject,'value');
s5=2.7183^(s5/10);
f55=s5*f5;
% hObject    handle to slider5 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'Value') returns position of slider
%        get(hObject,'Min') and get(hObject,'Max') to determine range of slider


% --- Executes during object creation, after setting all properties.
function slider5_CreateFcn(hObject, eventdata, handles)
% hObject    handle to slider5 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: slider controls usually have a light gray background.
if isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor',[.9 .9 .9]);
end


% --- Executes on slider movement.
function slider6_Callback(hObject, eventdata, handles)
global f6 s6 f66;
set(handles.text6,'string',num2str(get(hObject,'value')));
s6=get(hObject,'value');
s6=2.7183^(s6/10);
f66=s6*f6;
% hObject    handle to slider6 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'Value') returns position of slider
%        get(hObject,'Min') and get(hObject,'Max') to determine range of slider


% --- Executes during object creation, after setting all properties.
function slider6_CreateFcn(hObject, eventdata, handles)
% hObject    handle to slider6 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: slider controls usually have a light gray background.
if isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor',[.9 .9 .9]);
end


% --- Executes on slider movement.
function slider7_Callback(hObject, eventdata, handles)
global f7 s7 f77;
set(handles.text7,'string',num2str(get(hObject,'value')));
s7=get(hObject,'value');
s7=2.7183^(s7/10);
f77=s7*f7;
% hObject    handle to slider7 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'Value') returns position of slider
%        get(hObject,'Min') and get(hObject,'Max') to determine range of slider


% --- Executes during object creation, after setting all properties.
function slider7_CreateFcn(hObject, eventdata, handles)
% hObject    handle to slider7 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: slider controls usually have a light gray background.
if isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor',[.9 .9 .9]);
end


% --- Executes on button press in pushbutton1.
function pushbutton1_Callback(hObject, eventdata, handles)
global Fs NFFT nbits y f1 f2 f3 f4 f5 f6 f7 f8 f11 f22 f33 f44 f55 f66 f77 f88 bz1 bz2 bz3 bz4 bz5 bz6 bz7 bz8 az1 az2 az3 az4 az5 az6 az7 az8;
[filename,pathname] = uigetfile({'*.wav';'*.*'},'载入音频文件');
file = [pathname,filename];
[sound_wav,Fs,nbits] = wavread(file);
WaveLength = length(sound_wav);
NFFT = 2^nextpow2(WaveLength); 
y=sound_wav;
yy = fft(y,NFFT);%单声道
k=(1:NFFT)*Fs/NFFT;
axes(handles.axes1);
plot(k,yy);
axis([0 45000 -2000 2000]);
xlabel('频率（Hz）');
ylabel('幅度（V）');
f1=filter(bz1,az1,y);
f2=filter(bz2,az2,y);
f3=filter(bz3,az3,y);
f4=filter(bz4,az4,y);
f5=filter(bz5,az5,y);
f6=filter(bz6,az6,y);
f7=filter(bz7,az7,y);
f8=filter(bz8,az8,y);
f11=f1;
f22=f2;
f33=f3;
f44=f4;
f55=f5;
f66=f6;
f77=f7;
f88=f8;
% hObject    handle to pushbutton1 (see GCBO)
% eventdata  reserved - to be definewd in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)


% --- Executes on button press in pushbutton2.
function pushbutton2_Callback(hObject, eventdata, handles)
global Fs NFFT nbits y f1 f2 f3 f4 f5 f6 f7 f8 f11 f22 f33 f44 f55 f66 f77 f88 bz1 bz2 bz3 bz4 bz5 bz6 bz7 bz8 az1 az2 az3 az4 az5 az6 az7 az8;
fs=32000;  %取样频率
duration=str2num(get(handles.edit1,'String'));
y=wavrecord(duration*fs,fs);         %duration*fs 是总的采样点数
yy = fft(y,NFFT);%单声道
k=(1:NFFT)*Fs/NFFT;
axes(handles.axes1);
plot(k,yy);
axis([0 45000 -3000 3000]);
xlabel('频率（Hz）');
ylabel('幅度（V）');
f1=filter(bz1,az1,y);
f2=filter(bz2,az2,y);
f3=filter(bz3,az3,y);
f4=filter(bz4,az4,y);
f5=filter(bz5,az5,y);
f6=filter(bz6,az6,y);
f7=filter(bz7,az7,y);
f8=filter(bz8,az8,y);
f11=f1;
f22=f2;
f33=f3;
f44=f4;
f55=f5;
f66=f6;
f77=f7;
f88=f8;


% hObject    handle to pushbutton2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)


% --- Executes on button press in pushbutton3.
function pushbutton3_Callback(hObject, eventdata, handles)
global Fs NFFT nbits f11 f22 f33 f44 f55 f66 f77 f88 p pa s1 s2 s3 s4 s5 s6 s7 s8;
vol=(f11+f22+f33+f44+f55+f66+f77+f88);
p=audioplayer(vol,16000,nbits);
yyy = fft(vol,NFFT);%单声道
k=(1:NFFT)*Fs/NFFT;
axes(handles.axes2);
plot(k,yyy);
axis([0 45000 -3000 3000]);
xlabel('频率（Hz）');
ylabel('幅度（V）');
play(p);
pa=1;

% hObject    handle to pushbutton3 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)



function edit1_Callback(hObject, eventdata, handles)
global duration;
duration=get(str2num(handles.edit1,'String'));
% hObject    handle to edit1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of edit1 as text
%        str2double(get(hObject,'String')) returns contents of edit1 as a double


% --- Executes during object creation, after setting all properties.
function edit1_CreateFcn(hObject, eventdata, handles)
% hObject    handle to edit1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
%if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
%    set(hObject,'BackgroundColor','white');
%end


% --- Executes on slider movement.
function slider8_Callback(hObject, eventdata, handles)
global f8 s8 f88;
set(handles.text10,'string',num2str(get(hObject,'value')));
s8=get(hObject,'value');
s8=2.7183^(s8/10);
f88=2.7183^(s8/10)*f8;

% hObject    handle to slider8 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'Value') returns position of slider
%        get(hObject,'Min') and get(hObject,'Max') to determine range of slider


% --- Executes during object creation, after setting all properties.
function slider8_CreateFcn(hObject, eventdata, handles)
% hObject    handle to slider8 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: slider controls usually have a light gray background.
if isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor',[.9 .9 .9]);
end


% --- Executes on button press in pushbutton4.
function pushbutton4_Callback(hObject, eventdata, handles)
global p pa;
if pa
    pause(p);
    pa=0;
else
    resume(p);
    pa=1;
end

% hObject    handle to pushbutton4 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)


% --- Executes on button press in pushbutton5.
function pushbutton5_Callback(hObject, eventdata, handles)
% hObject    handle to pushbutton5 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
global n;   % 训练时记录的第几个话说人
global m;   % 测试时记录的第几个话说人
global code;
con_pbn=get(handles.pushbutton7,'value');
pbn=con_pbn;
if (pbn)||(n>=6)
    delete('train/*.wav');
    n=1;%从头开始
end
fs=11025;           %取样频率

duration=10;         %录音时间

% fprintf('Press any key to start %g seconds of recording...\n',duration);
% 
% pause;

fprintf('training...\n');
y=wavrecord(duration*fs,fs);         %duration*fs 是总的采样点数

% fprintf('Finished training.\n');

% str=num2str(file,n);

name=strcat('D:\MATLAB\blockframes\train\',...
            num2str(n),'.wav'); % n为全局变量,组合成字符串
wavwrite(y,fs,name);
% fprintf('Press any key to play the recording...\n');
% 
% pause;
wavplay(y,fs);
code=train('D:\MATLAB\blockframes\train\',n);
n=n+1;  %指向下一个说话人


% --- Executes on button press in pushbutton6.
function pushbutton6_Callback(hObject, eventdata, handles)
% hObject    handle to pushbutton6 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
global n;   % 训练时记录的第几个话说人
global m;   % 
global code;
fs=11025;
duration=4;
set(handles.edit2,'string','');
fprintf('testing...\n');

y=wavrecord(duration*fs,fs);
con_pbn=get(handles.pushbutton7,'value');
pbn=con_pbn
if (pbn)
    delete('test/*.wav');
    m=1;%从头开始
end

name=strcat('D:\MATLAB\blockframes\test\',...
            num2str(m),'.wav');
wavwrite(y,fs,name);
set(handles.edit2,'string','');
wavplay(y,fs);
result=test('D:\MATLAB\blockframes\test\',m,code);
disp(result);
m=m+1;
    if result==1 set(handles.edit2,'string','陈德');
    elseif result==2 set(handles.edit2,'string','郭功勋');
    elseif result==3 set(handles.edit2,'string','朱凌飞');
    elseif result==4 set(handles.edit2,'string','杨丹丹');
    elseif result==5 set(handles.edit2,'string','王翔');
    elseif result>=6 
        result = sprintf('与第 %d 个说话人匹配', result-5);
        set(handles.edit2,'string',result);
    end


% --- Executes on button press in pushbutton7.
function pushbutton7_Callback(hObject, eventdata, handles)
% hObject    handle to pushbutton7 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% --- Executes during object creation, after setting all properties.
function pushbutton7_CreateFcn(hObject, eventdata, handles)
% hObject    handle to pushbutton7 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% Hint: get(hObject,'Value') returns toggle state of pushbutton7


function edit2_Callback(hObject, eventdata, handles)
% hObject    handle to edit2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of edit2 as text
%        str2double(get(hObject,'String')) returns contents of edit2 as a double


% --- Executes during object creation, after setting all properties.
function edit2_CreateFcn(hObject, eventdata, handles)
% hObject    handle to edit2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


% --- Executes on button press in pushbutton9.
function pushbutton9_Callback(hObject, eventdata, handles)
% hObject    handle to pushbutton9 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
ncoeff = 13;          %Required number of mfcc coefficients
N = 20;               %Number of words in vocabulary
k = 3;                %Number of nearest neighbors to choose
fs=16000;             %Sampling rate 
duration1 = 0.15;     %Initial silence duration in seconds
duration2 = 2;        %Recording duration in seconds
G=2;                  %vary this factor to compensate for amplitude variations
NSpeakers = 5;        %Number of training speakers

fprintf('Press any key to start %g seconds of speech recording...', duration2); 
%pause;
silence = wavrecord(duration1*fs, fs);
fprintf('Recording speech...'); 
speechIn = wavrecord(duration2*fs, fs); % duration*fs is the total number of sample points 

fprintf('Finished recording.\n');
fprintf('System is trying to recognize what you have spoken...\n');
speechIn1 = [silence;speechIn];                  %pads with 150 ms silence
speechIn2 = speechIn1.*G;
speechIn3 = speechIn2 - mean(speechIn2);         %DC offset elimination
speechIn = nreduce(speechIn3,fs);     %Applies spectral subtraction




rMatrix1 = mfccf(ncoeff,speechIn,fs);            %Compute test feature vector
rMatrix = CMN(rMatrix1);                         %Removes convolutional noise

Sco = DTWScores(rMatrix,N);                      %computes all DTW scores
[SortedScores,EIndex] = sort(Sco);               %Sort scores increasing
K_Vector = EIndex(1:k);                          %Gets k lowest scores
Neighbors = zeros(1,k);                          %will hold k-N neighbors

%Essentially, code below uses the index of the returned k lowest scores to
%determine their classes

for t = 1:k
    u = K_Vector(t);
    for r = 1:NSpeakers-1
        if u <= (N)
            break
        else u = u - (N);
        end
    end
    Neighbors(t) = u;
    
end

%Apply k-Nearest Neighbor rule
Nbr = Neighbors
%sortk = sort(Nbr);
[Modal,Freq] = mode(Nbr);                              %most frequent value
Word = strvcat('off','blue','craft','day','eat','go','hand','ice','love','left','glass','yes','new','buy','rich','open','say','toy','fire','seven'); 
if mean(abs(speechIn)) < 0.01
    set(handles.edit4,'string','麦克风没有录到你的声音.');
elseif ((k/Freq) > 2)                                  %if no majority
    set(handles.edit4,'string','你所说的不能被正确识别');
else
    set(handles.edit4,'string',['    ' Word(Modal,:)]); %Prints recognized word
end



function edit4_Callback(hObject, eventdata, handles)
% hObject    handle to edit4 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of edit4 as text
%        str2double(get(hObject,'String')) returns contents of edit4 as a double


% --- Executes during object creation, after setting all properties.
function edit4_CreateFcn(hObject, eventdata, handles)
% hObject    handle to edit4 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
