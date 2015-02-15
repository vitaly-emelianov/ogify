/*
 * Author: Abdullah A Almsaeed
 * Date: 4 Jan 2014
 * Description:
 *      This is a demo file used only for the main dashboard (index.html)
 **/
"use strict";

$(function () {

  ////Activate the iCheck Plugin
  //$('input[type="checkbox"]').iCheck({
  //  checkboxClass: 'icheckbox_flat-blue',
  //  radioClass: 'iradio_flat-blue'
  //});
  //Make the dashboard widgets sortable Using jquery UI
  $(".sortableSwap").sortable({
   // placeholder: "sort-highlight",
    connectWith: ".connectedSortable",
    handle: ".box-header, .nav-tabs",
    forcePlaceholderSize: false,
    zIndex: 999999

  });

  $(".connectedSortable .box-header, .connectedSortable .nav-tabs-custom").css("cursor", "move");

  //jQuery UI sortable for the todo list
  $(".todo-list").sortable({
    placeholder: "sort-highlight",
    handle: ".handle",
    forcePlaceholderSize: true,
    zIndex: 999999
  }).disableSelection();

  //bootstrap WYSIHTML5 - text editor
  //$(".textarea").wysihtml5();

  //$('.daterange').daterangepicker(
  //        {
  //          ranges: {
  //            'Today': [moment(), moment()],
  //            'Yesterday': [moment().subtract('days', 1), moment().subtract('days', 1)],
  //            'Last 7 Days': [moment().subtract('days', 6), moment()],
  //            'Last 30 Days': [moment().subtract('days', 29), moment()],
  //            'This Month': [moment().startOf('month'), moment().endOf('month')],
  //            'Last Month': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')]
  //          },
  //          startDate: moment().subtract('days', 29),
  //          endDate: moment()
  //        },
  //function (start, end) {
  //  alert("You chose: " + start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
  //});

  /* jQueryKnob */
  //$(".knob").knob();

  //jvectormap data
  var visitorsData = {
    "US": 398, //USA
    "SA": 400, //Saudi Arabia
    "CA": 1000, //Canada
    "DE": 500, //Germany
    "FR": 760, //France
    "CN": 300, //China
    "AU": 700, //Australia
    "BR": 600, //Brazil
    "IN": 800, //India
    "GB": 320, //Great Britain
    "RU": 3000 //Russia
  };

  //World map by jvectormap
  //$('#world-map').vectorMap({
  //  map: 'world_mill_en',
  //  backgroundColor: "transparent",
  //  regionStyle: {
  //    initial: {
  //      fill: '#e4e4e4',
  //      "fill-opacity": 1,
  //      stroke: 'none',
  //      "stroke-width": 0,
  //      "stroke-opacity": 1
  //    }
  //  },
  //  series: {
  //    regions: [{
  //        values: visitorsData,
  //        scale: ["#92c1dc", "#ebf4f9"],
  //        normalizeFunction: 'polynomial'
  //      }]
  //  },
  //  onRegionLabelShow: function (e, el, code) {
  //    if (typeof visitorsData[code] != "undefined")
  //      el.html(el.html() + ': ' + visitorsData[code] + ' new visitors');
  //  }
  //});


  //Sparkline charts
  var myvalues = [1000, 1200, 920, 927, 931, 1027, 819, 930, 1021];
  $('#sparkline-1').sparkline(myvalues, {
    type: 'line',
    lineColor: '#92c1dc',
    fillColor: "#ebf4f9",
    height: '50',
    width: '80'
  });
  myvalues = [515, 519, 520, 522, 652, 810, 370, 627, 319, 630, 921];
  $('#sparkline-2').sparkline(myvalues, {
    type: 'line',
    lineColor: '#92c1dc',
    fillColor: "#ebf4f9",
    height: '50',
    width: '80'
  });
  myvalues = [15, 19, 20, 22, 33, 27, 31, 27, 19, 30, 21];
  $('#sparkline-3').sparkline(myvalues, {
    type: 'line',
    lineColor: '#92c1dc',
    fillColor: "#ebf4f9",
    height: '50',
    width: '80'
  });

  //The Calender
  $("#calendar").datepicker();

  //SLIMSCROLL FOR CHAT WIDGET
  $('#chat-box').slimScroll({
    height: '250px'
  });

  /* Morris.js Charts */
  // Sales chart
  var area = new Morris.Area({
    element: 'stat-chart',
    resize: true,
    data: [
      {y: '2015-02-06', created: 15, resolved: 15},
      {y: '2015-02-07', created: 23, resolved: 12},
      {y: '2015-02-08', created: 49, resolved: 18},
      {y: '2015-02-09', created: 53, resolved: 52},
      {y: '2015-02-10', created: 45, resolved: 30},
      {y: '2015-02-11', created: 38, resolved: 29},
      {y: '2015-02-12', created: 36, resolved: 28},
      {y: '2015-02-13', created: 68, resolved: 47},
      {y: '2015-02-14', created: 70, resolved: 53},
      {y: '2015-02-15', created: 75, resolved: 50}
    ],
    xkey: 'y',
    ykeys: ['created', 'resolved'],
    labels: ['Создано задач', 'Выполнено задач'],
    lineColors: ['#a0d0e0', '#3c8dbc'],
    hideHover: 'auto',
      xLabelFormat: function (x) {
          var IndexToMonth = [ "Jan", "Feb", "Mär", "Apr", "Mai", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dez" ];
          var month = IndexToMonth[ x.getMonth() ];
          var day = x.getDate();
          return day + ' ' + month;
      },
      dateFormat: function (x) {
          var IndexToMonth = [ "Jan", "Feb", "Mär", "Apr", "Mai", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dez" ];
          var month = IndexToMonth[ new Date(x).getMonth() ];
          var day = new Date(x).getDate();
          return day + ' ' + month;
      }
  });

  //Donut Chart
  var donut = new Morris.Donut({
    element: 'sales-chart',
    resize: true,
    colors: ["#3c8dbc", "#f56954", "#00a65a"],
    data: [
      {label: "Download Sales", value: 12},
      {label: "In-Store Sales", value: 30},
      {label: "Mail-Order Sales", value: 20}
    ],
    hideHover: 'auto'
  });

  //Fix for charts under tabs
  $('.box ul.nav a').on('shown.bs.tab', function (e) {
    area.redraw();
    donut.redraw();
  });


  /* BOX REFRESH PLUGIN EXAMPLE (usage with morris charts) */
  $("#loading-example").boxRefresh({
    source: "ajax/dashboard-boxrefresh-demo.php",
    onLoadDone: function (box) {
      var bar = new Morris.Bar({
        element: 'bar-chart',
        resize: true,
        data: [
          {y: '2006', a: 100, b: 90},
          {y: '2007', a: 75, b: 65},
          {y: '2008', a: 50, b: 40},
          {y: '2009', a: 75, b: 65},
          {y: '2010', a: 50, b: 40},
          {y: '2011', a: 75, b: 65},
          {y: '2012', a: 100, b: 90}
        ],
        barColors: ['#00a65a', '#f56954'],
        xkey: 'y',
        ykeys: ['a', 'b'],
        labels: ['CPU', 'DISK'],
        hideHover: 'auto'
      });
    }
  });

  /* The todo list plugin */
  $(".todo-list").todolist({
    onCheck: function (ele) {
      console.log("The element has been checked")
    },
    onUncheck: function (ele) {
      console.log("The element has been unchecked")
    }
  });

});
