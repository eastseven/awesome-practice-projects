function formRules(type) {
    var rules = {
        username: {
            required: true,
            maxlength: 50,
            remote: function () {
                var ajax = {
                    url : '/validate/customer',
                    type: 'post',
                    dataType: 'json',
                    data: {
                        field: 'username',
                        value: $('#username').val(),
                        origin: $('#username').attr('data')
                    }
                };
                return ajax;
            }
        },
        firstName: {required: true, maxlength: 50},
        lastName: {required: true, maxlength: 50},
        mobile: {required: true, maxlength: 50},
        email: {required: true, email: true, maxlength: 50},
    };

    return rules;
}

function add() {
    var url = '/customer/add';
    var dialog = $('#modal').empty().load(url, function (html) {
        var form = $(this).find('form');
        console.log(form);

        form.validate({
            rules: formRules(),
            submitHandler: function (form) {
                $(form).find('button[type=submit]').addClass('disabled');

                $(form).ajaxSubmit({
                    method: 'POST',
                    url: url,
                    success: function (data) {
                        bootbox.alert({
                            size: 'small', message: data.success ? '成功' : '失败'
                        });

                        $('#modal').modal('hide');
                        $('table').DataTable().ajax.reload();

                        $(form).find('button[type=submit]').removeClass('disabled');
                    },
                    error: function (xhr, resp, text) {
                        bootbox.alert(resp);
                        $('#modal').modal('hide');
                    }
                });
            }
        });
    });
    dialog.modal('show');
}

function edit(button) {
    var id = $(button).attr('data-id');
    var url = '/customer/edit/';
    var dialog = $('#modal').empty().load(url + id, function (html) {
        $(this).find('form').validate({
            rules: formRules(),
            submitHandler: function (form) {
                $(form).find('button[type=submit]').addClass('disabled');

                $(form).ajaxSubmit({
                    method: 'POST',
                    url: url,
                    success: function (data) {
                        bootbox.alert({
                            size: 'small', message: data.success ? '成功' : '失败'
                        });

                        $('#modal').modal('hide');
                        $('table').DataTable().ajax.reload();

                        $(form).find('button[type=submit]').removeClass('disabled');
                    },
                    error: function (xhr, resp, text) {
                        bootbox.alert(resp);
                        $('#modal').modal('hide');
                    }
                });
            }
        });
    });
    dialog.modal('show');
}

function del(button) {
    var id = $(button).attr('data-id');
    var url = '/customer/delete/' + id;
    bootbox.confirm('remove ?', function (result) {
        if (result) {
            $.getJSON(url, function (response) {
                $('table').DataTable().ajax.reload();
                var msg = response.success ? '成功' : '失败';
                bootbox.alert({size: 'small', message: msg});
            });
        }
    });
}

function view(button) {
    var id = $(button).attr('data-id');
    var url = '/customer/view/' + id;
    var dialog = $('#modal').empty().load(url, function (html) {

    });
    dialog.modal('show');
}

var customerColumns = [
    {'data': 'id'},
    {'data': 'image'},
    {'data': 'username'},
    {'data': 'createTime'},
    {'data': 'firstName'},
    {'data': 'lastName'},
    {'data': 'mobile'},
    {'data': 'email'},
    {
        'data': 'status', render: function (data, type, row) {
        //console.log(data, row);
        return data;
    }
    },
    {
        'data': 'id', orderable: false, render: function (data, type, row) {
        var btn = '';
        btn += ' <button class="btn btn-sm btn-primary" data-id="' + data + '" onclick="view(this)">查</button>';
        btn += ' <button class="btn btn-sm btn-primary" data-id="' + data + '" onclick="edit(this)">改</button>';
        btn += ' <button class="btn btn-sm btn-primary" data-id="' + data + '" onclick="del(this)">删</button>';
        return btn;
    }
    }
];

$(document).ready(function () {

    var table = $('#customer-table').DataTable({
        'filter': false,
        'processing': true,
        'serverSide': true,
        'language': {'url': '//res.dagteam.cn/json/datatables.net/Chinese.json'},
        'columns': customerColumns,
        'ajax': {
            'contentType': 'application/json',
            'url': '/data/customers',
            'type': 'POST',
            'data': function (d) {
                return JSON.stringify(d);
            }
        },
        'order': [[0, 'desc']]
    });
});