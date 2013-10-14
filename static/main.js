var posters = [
    "http://www.infoq.com/resource/presentations/website-outages/en/mediumimage/Sidbig.jpg",
    "http://www.infoq.com/resource/presentations/wearable-technology/en/mediumimage/Rainbig.jpg",
    "http://www.infoq.com/resource/presentations/team-hiring-techniques/en/mediumimage/Petebig.jpg",
    "http://www.infoq.com/resource/presentations/team-happiness-culture/en/mediumimage/Danbig.jpg",
    "http://www.infoq.com/resource/presentations/sugarj/en/mediumimage/Sebbig.jpg",
    "http://www.infoq.com/resource/presentations/stratos-app-factory-tutorial/en/mediumimage/Paulbig.jpg",
    "http://www.infoq.com/resource/presentations/statoil-budgeting/en/mediumimage/Bjabig.jpg",
    "http://www.infoq.com/resource/presentations/shutl-neo4j/en/mediumimage/Volbig.jpg",
    "http://www.infoq.com/resource/presentations/scaling-reddit/en/mediumimage/Jerbig.jpg",
    "http://www.infoq.com/resource/presentations/scalability-data-mining/en/mediumimage/Andrewbig.jpg",
    "http://www.infoq.com/resource/presentations/scalability-case-study/en/mediumimage/Jesperbig.jpg",
    "http://www.infoq.com/resource/presentations/scala-enterprise-introduction/en/mediumimage/Peterbig.jpg",
    "http://www.infoq.com/resource/presentations/robust-software/en/mediumimage/Chrisbig.jpg",
    "http://www.infoq.com/resource/presentations/ritz-clojure/en/mediumimage/Duncanbig.jpg",
    "http://www.infoq.com/resource/presentations/rest-cloud-riverbed/en/mediumimage/Chrisbig.jpg",
    "http://www.infoq.com/resource/presentations/recruiting-techniques/en/mediumimage/treibig.jpg",
    "http://www.infoq.com/resource/presentations/racket/en/mediumimage/Matbig.jpg",
    "http://www.infoq.com/resource/presentations/python-hadoop/en/mediumimage/Uribig.jpg",
    "http://www.infoq.com/resource/presentations/post-functional-scala-clojure-haskell/en/mediumimage/Danielbig.jpg",
    "http://www.infoq.com/resource/presentations/polymorphism-functional-oop/en/mediumimage/Crebig.jpg",
    "http://www.infoq.com/resource/presentations/polyglot-javascript/en/mediumimage/Alonbig.jpg",
    "http://www.infoq.com/resource/presentations/piplin/en/mediumimage/Greenbig.jpg",
    "http://www.infoq.com/resource/presentations/paypal-stack/en/mediumimage/Billbig.jpg",
    "http://www.infoq.com/resource/presentations/pallet/en/mediumimage/Duncanbig.jpg",
    "http://www.infoq.com/resource/presentations/paas-cloud-foundry/en/mediumimage/Andybig.jpg",
    "http://www.infoq.com/resource/presentations/optimize-hadoop-jobs/en/mediumimage/Micbig.jpg",
    "http://www.infoq.com/resource/presentations/open-source-silos-scalability/en/mediumimage/Theobig.jpg",
    "http://www.infoq.com/resource/presentations/open-source/en/mediumimage/Peterbig.jpg",
    "http://www.infoq.com/resource/presentations/nfc/en/mediumimage/Neilbig.jpg",
    "http://www.infoq.com/resource/presentations/netflix-resiliency-failure-cloud/en/mediumimage/Aribig.jpg",
    "http://www.infoq.com/resource/presentations/net-java-legacy-techniques/en/mediumimage/Robertbig.jpg",
    "http://www.infoq.com/resource/presentations/narrow-test-java-ruby/en/mediumimage/Fabbig.jpg",
    "http://www.infoq.com/resource/presentations/mysql-scalability/en/mediumimage/Peterbig.jpg",
    "http://www.infoq.com/resource/presentations/music-functional-language/en/mediumimage/Chrisbig.jpg",
    "http://www.infoq.com/resource/presentations/mobile-smartphone-sensors/en/mediumimage/Adambig.jpg",
    "http://www.infoq.com/resource/presentations/mdsd-mainstream/en/mediumimage/Jurbig.jpg",
    "http://www.infoq.com/resource/presentations/managing-serendipity/en/mediumimage/Davebig.jpg",
    "http://www.infoq.com/resource/presentations/macros-clojure-west-2013/en/mediumimage/Garybig.jpg",
    "http://www.infoq.com/resource/presentations/logmein-scale/en/mediumimage/Ancabig.jpg",
    "http://www.infoq.com/resource/presentations/learning-developer/en/mediumimage/Nolenbig.jpg",
    "http://www.infoq.com/resource/presentations/lean-cross-functional-pairing/en/mediumimage/Jonbig.jpg",
    "http://www.infoq.com/resource/presentations/jvm-future-parallelism-cores/en/mediumimage/panelbig.jpg",
    "http://www.infoq.com/resource/presentations/job-interview-tips/en/mediumimage/Danbig.jpg",
    "http://www.infoq.com/resource/presentations/javascript-testing/en/mediumimage/Markbig.jpg",
    "http://www.infoq.com/resource/presentations/java-8-lambda-qcon-ny-2013/en/mediumimage/Stebig.jpg",
    "http://www.infoq.com/resource/presentations/interactive-art/en/mediumimage/Rembig.jpg",
    "http://www.infoq.com/resource/presentations/html5-finance-migration/en/mediumimage/2big.jpg",
    "http://www.infoq.com/resource/presentations/holystic-assumption/en/mediumimage/Gifbig.jpg",
    "http://www.infoq.com/resource/presentations/hiring-tips/en/mediumimage/Marbig.jpg",
    "http://www.infoq.com/resource/presentations/guardian-content-api/en/mediumimage/Michaelbig.jpg",
    "http://www.infoq.com/resource/presentations/gstreamer-sdk-android/en/mediumimage/Xavibig.jpg",
    "http://www.infoq.com/resource/presentations/graph-database-theory/en/mediumimage/Jimbig.jpg",
    "http://www.infoq.com/resource/presentations/gof-patterns-c-plus-plus-boost/en/mediumimage/Tobiasbig.jpg",
    "http://www.infoq.com/resource/presentations/git-index/en/mediumimage/Chabig.jpg",
    "http://www.infoq.com/resource/presentations/games-scalability-omgpop/en/mediumimage/Robinbig.jpg",
    "http://www.infoq.com/resource/presentations/functional-async/en/mediumimage/Jimbig.jpg",
    "http://www.infoq.com/resource/presentations/firefox-large-javascript-project/en/mediumimage/Antonbig.jpg",
    "http://www.infoq.com/resource/presentations/etsy-release-cycle/en/mediumimage/Stebig.jpg",
    "http://www.infoq.com/resource/presentations/erlang-clojure/en/mediumimage/Reidbig.jpg",
    "http://www.infoq.com/resource/presentations/embedded-java-mqtt/en/mediumimage/Peterbig.jpg",
    "http://www.infoq.com/resource/presentations/elastic-r-data/en/mediumimage/Karimbig.jpg",
    "http://www.infoq.com/resource/presentations/eharmony-hadoop/en/mediumimage/Vacbig.jpg",
    "http://www.infoq.com/resource/presentations/data-structures-distribution/en/mediumimage/Scottbig.jpg",
    "http://www.infoq.com/resource/presentations/crowdtap-continuous-deployment/en/mediumimage/Karbig.jpg",
    "http://www.infoq.com/resource/presentations/continuous-delivery-azure/en/mediumimage/Justinbig.jpg",
    "http://www.infoq.com/resource/presentations/concatenative-clojure/en/mediumimage/Brandonbig.jpg",
    "http://www.infoq.com/resource/presentations/code-review-critique/en/mediumimage/Arjbig.jpg",
    "http://www.infoq.com/resource/presentations/code-cloning/en/mediumimage/Raibig.jpg",
    "http://www.infoq.com/resource/presentations/cloud-infrastructure-cost/en/mediumimage/Richardbig.jpg",
    "http://www.infoq.com/resource/presentations/cloud-data-teaching-research/en/mediumimage/Karbig.jpg",
    "http://www.infoq.com/resource/presentations/cloud-compare/en/mediumimage/Paulbig.jpg",
    "http://www.infoq.com/resource/presentations/clojure-robots/en/mediumimage/Meierbig.jpg",
    "http://www.infoq.com/resource/presentations/clojure-infrastructure/en/mediumimage/Batbig.jpg",
    "http://www.infoq.com/resource/presentations/clojure-data-state-value/en/mediumimage/Alexbig.jpg",
    "http://www.infoq.com/resource/presentations/clojure-clojurescript/en/mediumimage/Romanbig.jpg",
    "http://www.infoq.com/resource/presentations/cljs-in-cljs/en/mediumimage/Jobig.jpg",
    "http://www.infoq.com/resource/presentations/c-plus-plus-tests/en/mediumimage/Alanbig.jpg",
    "http://www.infoq.com/resource/presentations/big-data-analysis/en/mediumimage/Rebecabig.jpg",
    "http://www.infoq.com/resource/presentations/banking-performance-trading/en/mediumimage/Johnbig.jpg",
    "http://www.infoq.com/resource/presentations/app-arch-groovy/en/mediumimage/Danielbig.jpg",
    "http://www.infoq.com/resource/presentations/analyze-running-system/en/mediumimage/Zacbig.jpg",
    "http://www.infoq.com/resource/presentations/amqp-soundcloud/en/mediumimage/doibig.jpg",
    "http://www.infoq.com/resource/presentations/amazon-dynamodb-patterns-practices/en/mediumimage/Sivabig.jpg",
    "http://www.infoq.com/resource/presentations/agile-citizen-engineers-world/en/mediumimage/Ryanbig.jpg",
    "http://www.infoq.com/resource/presentations/UX-mobile-consumerization/en/mediumimage/Chrisbig.jpg",
    "http://www.infoq.com/resource/presentations/Scaling-Dropbox/en/mediumimage/Rajbig.jpg",
    "http://www.infoq.com/resource/presentations/Raspberry-Pi/en/mediumimage/Stevebig.jpg",
    "http://www.infoq.com/resource/presentations/RText-DSL/en/mediumimage/Martinbig.jpg",
    "http://www.infoq.com/resource/presentations/Netflix-API-rxjava-hystrix/en/mediumimage/Crisbig.jpg",
    "http://www.infoq.com/resource/presentations/Model-Driven-SOA/en/mediumimage/Chrisbig.jpg",
    "http://www.infoq.com/resource/presentations/Macros-Monads/en/mediumimage/Doibig.jpg",
    "http://www.infoq.com/resource/presentations/Lean-Mindset-side-paradox/en/mediumimage/Marybig.jpg",
    "http://www.infoq.com/resource/presentations/HTTP-Performance/en/mediumimage/Poulbig.jpg",
    "http://www.infoq.com/resource/presentations/Expression-of-Ideas/en/mediumimage/Susbig.jpg",
    "http://www.infoq.com/resource/presentations/Design-Composition-Performance/en/mediumimage/Richbig.jpg",
    "http://www.infoq.com/resource/presentations/Design-Complexity-Agile/en/mediumimage/finalbig.jpg",
    "http://www.infoq.com/resource/presentations/Custom-Components-Android/en/mediumimage/Paulbig.jpg",
    "http://www.infoq.com/resource/presentations/Core-logic-SQL-ORM/en/mediumimage/Brobig.jpg",
    "http://www.infoq.com/resource/presentations/CIM-continuous-delivery/en/mediumimage/Jobig.jpg",
    "http://www.infoq.com/resource/presentations/Asynchronous-Scala-Java/en/mediumimage/Heatherbig.jpg"
];

var posters_local = [
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Sidbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Rainbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Petebig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Danbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Sebbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Paulbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Bjabig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Volbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Jerbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Andrewbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Jesperbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Peterbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Chrisbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Duncanbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Chrisbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/treibig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Matbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Uribig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Danielbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Crebig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Alonbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Greenbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Billbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Duncanbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Andybig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Micbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Theobig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Peterbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Neilbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Aribig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Robertbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Fabbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Peterbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Chrisbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Adambig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Jurbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Davebig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Garybig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Ancabig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Nolenbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Jonbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/panelbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Danbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Markbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Stebig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Rembig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/2big.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Gifbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Marbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Michaelbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Xavibig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Jimbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Tobiasbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Chabig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Robinbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Jimbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Antonbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Stebig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Reidbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Peterbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Karimbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Vacbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Scottbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Karbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Justinbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Brandonbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Arjbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Raibig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Richardbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Karbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Paulbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Meierbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Batbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Alexbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Romanbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Jobig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Alanbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Rebecabig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Johnbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Danielbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Zacbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/doibig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Sivabig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Ryanbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Chrisbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Rajbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Stevebig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Martinbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Crisbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Chrisbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Doibig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Marybig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Poulbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Susbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Richbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/finalbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Paulbig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Brobig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Jobig.jpg",
    "file:///Users/uwe/dev/infoq-podcast/static/resource/presentations/Heatherbig.jpg"
];

var amount = 30;
var columns = 5;
var rows = int(amount / columns);
var border = 3;
var width = 135;
var height = 100;

var w = width + border;
var h = height + border;
var ctx = createContext(columns * w, rows * h);
var counter = 0;

window.onload = function () {
  posters.slice(0, amount).forEach(function (poster, i) {
    var tmp = new Image();
    tmp.addEventListener("load", function () {
      var x = (i % columns) * w;
      var y = int(i / columns) * h;
      ctx.drawImage(this, x, y, width, height);
      maybeDone();
    }, false);
    tmp.src = poster;
  });
};

function maybeDone() {
  if (++counter < 10) return;

  var img_url = ctx.canvas.toDataURL();
  var wall = document.getElementById("wall")
  wall.style.backgroundImage = "url(" + img_url + ")";
}

function createContext(width, height) {
  var canvas = document.createElement("canvas");
  canvas.width = width;
  canvas.height = height;
  return canvas.getContext("2d");
}

function int(f) {
 return f | 0;
}
