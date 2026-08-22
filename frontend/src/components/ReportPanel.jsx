import StatsSummary from "./StatsSummary";
import ReportText from "./ReportText";
import FavoriteButton from "./FavoriteButton";

function today() {
	return new Intl.DateTimeFormat("ko-KR", { year: "numeric", month: "numeric", day: "numeric" }).format(
		new Date()
	);
}

export default function ReportPanel({ result }) {
	const { report, stats } = result;

	return (
		<section className="report-panel">
			<StatsSummary stats={stats} />
			<div className="report-panel-header">
				<h2>AI 분석 리포트</h2>
				<span className="report-panel-date">{today()}</span>
			</div>
			<ReportText text={report} />
			<FavoriteButton region={stats.regionName} industry={stats.industryKeyword} />
		</section>
	);
}
