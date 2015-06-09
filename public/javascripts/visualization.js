$(document).ready(function () {
    var nodes = [];
    var edges = [];
    var allMessages = {};
    var allNodes = {};
    var dfsStartingNodes = [];

    for (var i = 0; i < relations.length; i++) {
        if (relations[i]["id1"] == -1) {
            dfsStartingNodes.push(relations[i]["id2"]);
        }
    }

    for (var i = 0; i < messages.length; i++) {
        var message = messages[i];
        message["visited"] = false;
        allMessages[messages[i]["id"].toString()] = message;
    }

    allMessages["-1"] = {"id": -1, "sender": "start", "receiver": null, "visited": false};

    var edgesNum = 0;

    var dfs = function (node, trace) {
        node["visited"] = true;
        var sender = node["sender"];
        var receiver = node["receiver"];

        var senderName = "trace" + trace + "#" + sender;
        if (!allNodes[senderName]) {
            allNodes[senderName] = true;
            nodes.push({
                "id": senderName,
                "label": sender,
                "color": "#f00",
                "size": 1,
                x: Math.random(),
                y: Math.random()
            });
        }

        var receiverName = "trace" + trace + "#" + receiver;
        if (!allNodes[receiverName]) {
            allNodes[receiverName] = true;
            nodes.push({
                "id": receiverName,
                "label": receiver,
                "color": "#f00",
                "size": 1,
                x: Math.random(),
                y: Math.random()
            });
        }

        edges.push({
            "id": "trace" + trace + "#edge" + edgesNum++,
            "source": senderName,
            "target": receiverName,
            "color": "#000",
            "type": 'curvedArrow',
            "size": 50
        });

        for (var i = 0; i < relations.length; i++) {
            var newNode = allMessages[relations[i]["id2"]];
            if (relations[i]["id1"] == node["id"] && !newNode["visited"]) {
                dfs(newNode, trace);
            }
        }
    };

    var traceNum = 0;

    for (var i in dfsStartingNodes) {
        var n = dfsStartingNodes[i];
        var receiver = allMessages[n]["sender"];
        allMessages["-1"]["receiver"] = receiver;
        var senderName = "trace" + traceNum + "#" + "start";
        var receiverName = "trace" + traceNum + "#" + receiver;
        nodes.push({
            "id": senderName,
            "label": "start",
            "size": 0,
            "color": "#000",
            x: Math.random(),
            y: Math.random()
        });
        allNodes[senderName] = true;
        nodes.push({
            "id": receiverName,
            "label": receiver,
            "color": "#f00",
            "size": 1,
            x: Math.random(),
            y: Math.random()
        });
        allNodes[receiverName] = true;
        edges.push({
            "id": "trace" + traceNum + "#edge" + edgesNum++,
            "source": senderName,
            "target": receiverName,
            "color": "#000",
            "type": 'curvedArrow',
            "size": 50
        });
        dfs(allMessages[n], traceNum++);
    }

    console.log(nodes);
    console.log(edges);

    var s = new sigma({
        renderer: {
            container: document.getElementById('graph'),
            type: 'canvas'
        },
        "graph": {
            "nodes": nodes,
            "edges": edges
        }
    });

    s.startForceAtlas2({"adjustSizes": true, "edgeWeightInfluence": 0, "gravity": 10});

    setTimeout(function () {
        s.stopForceAtlas2();
    }, 5000);
});
