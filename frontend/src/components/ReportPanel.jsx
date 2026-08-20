import StatsSummary from "./StatsSummary";
import ReportText from "./ReportText";
import FavoriteButton from "./FavoriteButton";

export default function ReportPanel({ result }) {
	const { report, stats } = result;

	return (
		<section className="report-panel">
			<StatsSummary stats={stats} />
			<ReportText text={report} />
			<FavoriteButton region={stats.regionName} industry={stats.industryKeyword} />
		</section>
	);
}
