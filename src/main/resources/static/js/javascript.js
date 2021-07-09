let nameInput = $('#name');
let usernameInput = $('#username');
let roomInput = $('#room-id');
let contactList=document.querySelector("#contactList");
let groupList=$("#groupList");
let loader=$(".loader-contaner");
let usernamePage = document.querySelector('#username-page');
let chatPage = document.querySelector('#chat-page');
let usernameForm = document.querySelector('#usernameForm');
let messageForm = document.querySelector('#messageForm');
let messageInput = document.querySelector('#message');
let messageArea = document.querySelector('#messageArea');
let connectingElement = document.querySelector('.connecting');
let roomIdDisplay = document.querySelector('#room-id-display');
let listElement=document.querySelector(".list-group-item");
let buttonDisabled=false;
let stompClient = null;
let currentSubscription;
let username = null;
let name=null;
let currentChat=null;
let roomId = null;
let topic = null;
loader.hide();
let isExistsUsername=false;
let colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    loader.show();
    name = usernameInput.val().trim();
    username = usernameInput.val().trim();
        Cookies.set('name', username);
        if (username&&name) {
            usernamePage.classList.add('hidden');
            let socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);

            stompClient.connect({}, onConnected, onError);
            event.preventDefault();

        }

}

function enterRoom(newRoomId) {
    roomId = newRoomId;
    let message={};
    if (roomId==="home")
    {
        Cookies.set('home', roomId);
        connectingElement.classList.add('hidden');
        message={
            name:$("#name").val(),
            username:$("#username").val()
        };

        sessionStorage.setItem("name",message.name);
        sessionStorage.setItem("username",message.username);
    }
    else {
        $("#messageForm").show();
        if (sessionStorage.getItem("chatType")==="ONE-TO-ONE-CHAT"){
            message={
                message:"Ulandi",
                createdAt:new Date().toLocaleTimeString()+"T"+new Date().toLocaleDateString(),
                type:"JOIN",
                fromId:sessionStorage.getItem("currentUserId"),
                toId:sessionStorage.getItem("toUserId")
            }
        }else {
            message={
                message:"Ulandi",
                createdAt:new Date().toLocaleTimeString()+"T"+new Date().toLocaleDateString(),
                type:"JOIN",
                fromId:sessionStorage.getItem("currentUserId"),
                groupId:sessionStorage.getItem("currentGroupId")
            }
        }
    }



    topic = `/app/chat/${newRoomId}`;

    if (currentSubscription) {
        currentSubscription.unsubscribe();
    }
    currentSubscription = stompClient.subscribe(`/channel/${roomId}`,onMessageReceived);
    roomId==="home"?topic+='/addUser':topic;
    stompClient.send(topic,
        {},
        JSON.stringify(message)
    )
}


function addGroup(){
    topic = `/app/chat/home/addGroup`;

    if (currentSubscription) {
        currentSubscription.unsubscribe();
    }
    let message={
        groupName:$("#groupNameInput").val(),
        groupUsername:$("#groupUsernameInput").val()
    };
    currentSubscription = stompClient.subscribe(`/channel/home`,onMessageReceived);
    stompClient.send(topic,
        {},
        JSON.stringify(message)
    );

}

function onConnected() {
    enterRoom("home");
    $("#messageForm").hide();
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function sendMessage(event) {
    let messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        if (sessionStorage.getItem("chatType")==="ONE-TO-ONE-CHAT"){
            let message={
                message:$("#message").val(),
                createdAt:new Date().toLocaleTimeString()+"T"+new Date().toLocaleDateString(),
                type:"CHAT",
                fromId:sessionStorage.getItem("currentUserId"),
                toId:sessionStorage.getItem("toUserId")
            };
            stompClient.send(`${topic}/sendMessage`, {}, JSON.stringify(message));
        }
        else {
            let message={
                message:$("#message").val(),
                createdAt:new Date().toLocaleTimeString()+"T"+new Date().toLocaleDateString(),
                type:"CHAT",
                fromId:sessionStorage.getItem("currentUserId"),
                groupId:sessionStorage.getItem("currentGroupId")
            };
            stompClient.send(`${topic}/sendMessage`, {}, JSON.stringify(message));
        }
    }
    messageInput.value = '';
    event.preventDefault();
}

function enterChat(value) {
    console.log("li clicked=> ",value);
    event.preventDefault();
}
async function getStatus(value) {
    const res= await axios.get("/api/chanel/existsByUsername?username="+value);
    return res
}
async function existsByUsername(value) {
   buttonDisabled = getStatus(value);

}

function onMessageReceived(payload) {

    let message = JSON.parse(payload.body);
    chatPage.classList.remove('hidden');
    loader.hide();
    if (roomId==='home'){
        contactList.innerHTML="";
        console.log(message.userList);
        console.log("kirishda response=>",message);
        if (message.userList!==null){
            message.userList.map((item)=>{
                if(item.username===sessionStorage.getItem("username")){
                    $("#currentUserName").text(item.name);
                    sessionStorage.setItem("currentUserId",item.id);
                }
                else {
                    let userCont=document.createElement('li');
                    userCont.classList.add('list-group-item');
                    axios.get("/api/chanel/name?username1="+sessionStorage.getItem("username")+"&username2="+item.username).then((r)=>{
                        console.log(r.data);
                        userCont.id=r.data.name+"_"+item.id;
                    });
                    userCont.style.display="flex";
                    let userImgC=document.createElement('div');
                    userImgC.classList.add('col-xs-12');
                    userImgC.classList.add('col-sm-3');
                    let userInfoC=document.createElement('div');
                    userInfoC.classList.add('name');
                    userInfoC.classList.add('col-xs-12');
                    userInfoC.classList.add('col-sm-9');
                    let span=document.createElement('span');
                    span.innerText="Ism: "+item.name;
                    let br=document.createElement('br');
                    // let p=document.createElement('p');
                    // p.innerText="username: "+item.username;
                    let img=document.createElement('img');
                    img.classList.add("img-responsive");
                    img.classList.add("img-circle");
                    img.src="https://png.pngtree.com/element_our/png_detail/20181214/beard-man-icon-png_269198.jpg";
                    userImgC.appendChild(img);
                    userInfoC.appendChild(span);
                    userInfoC.appendChild(br);
                    // userInfoC.appendChild(p);
                    userCont.appendChild(userImgC);
                    userCont.appendChild(userInfoC);
                    contactList.appendChild(userCont);
                }
            });
        }
        if (message.groups){
            let group="";
            message.groups.map((item)=>{
                group+="<li id=\""+item.groupUsername+"_"+item.id+"\" class=\"list-group-item\" style=\"display: flex\">\n" +
                    "                                        <div class=\"col-xs-12 col-sm-3\">\n" +
                    "                                            <img src=\"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRA8r_sFDwky4HXW8x2hSMIy7VLsOtTqUmFGQ&usqp=CAU\" alt=\"Jean Myers\" class=\"img-responsive img-circle\" />\n" +
                    "                                        </div>\n" +
                    "                                        <div class=\"name col-xs-12 col-sm-9\">\n" +
                    "                                            <span>"+item.groupName+"</span><br/>\n" +
                    "                                        </div>\n" +
                    "                                    </li>"
            });
            $("#groupList").html(group);
        }

    }
    else {
        let messages = message.messages;
        messageArea.innerHTML="";
        if (messages.length===0){
            let messageElement =document.createElement('li');
            messageElement.classList.add('event-message');
            messageElement.innerText ='Xabarlar mavjud emas';
            messageArea.appendChild(messageElement);
        }
        else {
            messages.map((item)=>{
                let from = item.from;
                console.log("fromUser=>",from);
                let to=item.to;
                let createdAt=item.createdAt;
                let msg=item.message;
                let messageElement =document.createElement('li');
                if (item.type === 'JOIN') {
                    messageElement.classList.add('event-message');
                    messageElement.innerText = from.name + ' ulandi!';
                } else if (item.type === 'LEAVE') {
                    messageElement.classList.add('event-message');
                    messageElement.innerText = from.name + ' Tark etdi!';
                } else
                {

                    if (from.username===sessionStorage.getItem("username")){
                        messageElement.classList.add('chat-message1');
                        let usernameElement = document.createElement('span');
                        let usernameText = document.createTextNode(from.name);
                        usernameElement.appendChild(usernameText);
                        messageElement.appendChild(usernameElement);

                        let textElement = document.createElement('p');
                        let messageText = document.createTextNode(msg);
                        textElement.appendChild(messageText);
                        messageElement.appendChild(textElement);

                        let avatarElement = document.createElement('i');
                        let avatarText = document.createTextNode(from.name[0]);
                        avatarElement.appendChild(avatarText);
                        avatarElement.style['background-color'] = getAvatarColor(from.name);
                        messageElement.appendChild(avatarElement);

                    }else {
                        messageElement.classList.add('chat-message');
                        let avatarElement = document.createElement('i');
                        let avatarText = document.createTextNode(from.name[0]);
                        avatarElement.appendChild(avatarText);
                        avatarElement.style['background-color'] = getAvatarColor(from.name);

                        messageElement.appendChild(avatarElement);

                        let usernameElement = document.createElement('span');
                        let usernameText = document.createTextNode(from.name);
                        usernameElement.appendChild(usernameText);
                        messageElement.appendChild(usernameElement);

                        let textElement = document.createElement('p');
                        let messageText = document.createTextNode(msg);
                        textElement.appendChild(messageText);
                        messageElement.appendChild(textElement);
                    }

                }

                messageArea.appendChild(messageElement);
                messageArea.scrollTop = messageArea.scrollHeight;
            })
        }
    }
}

function getAvatarColor(messageSender) {
    let hash = 0;
    for (let i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    let index = Math.abs(hash % colors.length);
    return colors[index];
}

$(document).ready(function() {
    usernameForm.addEventListener('submit', connect, true);
    messageForm.addEventListener('submit', sendMessage, true);
    listElement.addEventListener("onclick",enterChat,false)
});

