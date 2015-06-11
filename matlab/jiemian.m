function varargout = jiemian(varargin)
% JIEMIAN MATLAB code for jiemian.fig
%      JIEMIAN, by itself, creates a new JIEMIAN or raises the existing
%      singleton*.
%
%      H = JIEMIAN returns the handle to a new JIEMIAN or the handle to
%      the existing singleton*.
%
%      JIEMIAN('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in JIEMIAN.M with the given input arguments.
%
%      JIEMIAN('Property','Value',...) creates a new JIEMIAN or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before jiemian_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to jiemian_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help jiemian

% Last Modified by GUIDE v2.5 13-Oct-2014 10:04:23

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @jiemian_OpeningFcn, ...
                   'gui_OutputFcn',  @jiemian_OutputFcn, ...
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
% global adress;
% adress='d:\Program Files\MATLAB704\work\speaker\';


% --- Executes just before jiemian is made visible.
function jiemian_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to jiemian (see VARARGIN)

% Choose default command line output for jiemian
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);

% UIWAIT makes jiemian wait for user response (see UIRESUME)
% uiwait(handles.figure1);


% --- Outputs from this function are returned to the command line.
function varargout = jiemian_OutputFcn(hObject, eventdata, handles) 
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

% --- Executes on button press in pushbutton1.
function pushbutton1_Callback(hObject, eventdata, handles)
% hObject    handle to pushbutton1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
global n;   % 训练时记录的第几个话说人
global m;   % 测试时记录的第几个话说人
global code;
con_pbn=get(handles.pushbutton4,'value');
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

% --- Executes on button press in pushbutton2.
function pushbutton2_Callback(hObject, eventdata, handles)
% hObject    handle to pushbutton2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
global n;   % 训练时记录的第几个话说人
global m;   % 
global code;
fs=11025;
duration=4;
set(handles.edit1,'string','');
fprintf('testing...\n');

y=wavrecord(duration*fs,fs);
con_pbn=get(handles.pushbutton4,'value');
pbn=con_pbn
if (pbn)
    delete('test/*.wav');
    m=1;%从头开始
end

name=strcat('D:\MATLAB\blockframes\test\',...
            num2str(m),'.wav');
wavwrite(y,fs,name);
set(handles.edit1,'string','');
wavplay(y,fs);
result=test('D:\MATLAB\blockframes\test\',m,code);
disp(result);
m=m+1;
    if result==1 set(handles.edit1,'string','陈德');
    elseif result==2 set(handles.edit1,'string','郭功勋');
    elseif result==3 set(handles.edit1,'string','朱凌飞');
    elseif result==4 set(handles.edit1,'string','杨丹丹');
    elseif result==5 set(handles.edit1,'string','王翔');
    elseif result>=6 
        result = sprintf('与第 %d 个说话人匹配', result-5);
        set(handles.edit1,'string',result);
    end
    

function edit1_Callback(hObject, eventdata, handles)
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
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end

% --- Executes on button press in pushbutton4.
function pushbutton4_Callback(hObject, eventdata, handles)
% hObject    handle to pushbutton4 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% Hint: get(hObject,'Value') returns toggle state of pushbutton4


% --- Executes during object creation, after setting all properties.
function pushbutton4_CreateFcn(hObject, eventdata, handles)
% hObject    handle to pushbutton4 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called
