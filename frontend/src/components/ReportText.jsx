export default function ReportText({ text }) {
	return (
		<article className="report-text">
			{text.split("\n").map((line, i) => (line.trim() ? <p key={i}>{line}</p> : null))}
		</article>
	);
}
